/*
 * Copyright 2020 Zetyun
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.zetyun.streamtau.expr.annotation;

import com.google.auto.service.AutoService;
import com.google.common.collect.ImmutableSet;
import com.squareup.javapoet.ClassName;
import com.squareup.javapoet.CodeBlock;
import com.squareup.javapoet.FieldSpec;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;
import java.util.Set;
import java.util.stream.Collectors;
import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.ElementFilter;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class EvaluatorsProcessor extends AbstractProcessor {
    private static final String EVAL_METHOD_NAME = "eval";
    private static final String LOOKUP_VAR_NAME = "lookup";
    private static final String TYPES_VAR_NAME = "types";
    private static final String INSTANCE_VAR_NAME = "INS";

    private static @NotNull String getSimpleName(@NotNull TypeName type) {
        String name = type.toString()
            .replaceAll("<.*>", "")
            .replace("[]", "Array");
        return name.substring(name.lastIndexOf('.') + 1);
    }

    private static @NotNull String getClassName(String name, String suffix) {
        return StringUtils.capitalize(name) + suffix;
    }

    private static @NotNull String getSubPackageName(@NotNull Element element) {
        String name = element.getSimpleName().toString();
        int pos = name.indexOf("Evaluators");
        if (pos > 0) {
            name = name.substring(0, pos);
        }
        return name.toLowerCase();
    }

    private static TypeName getBoxedType(@NotNull TypeName type) {
        return type.isPrimitive() ? type.box() : type;
    }

    private static List<String> getParaNameList(@NotNull ExecutableElement element) {
        return element.getParameters().stream()
            .map(VariableElement::getSimpleName)
            .map(Name::toString)
            .collect(Collectors.toList());
    }

    private static List<TypeName> getParaTypeList(@NotNull ExecutableElement element) {
        return element.getParameters().stream()
            .map(VariableElement::asType)
            .map(TypeName::get)
            .map(EvaluatorsProcessor::getBoxedType)
            .collect(Collectors.toList());
    }

    private static @NotNull String getEvaluatorId(List<TypeName> paraTypes) {
        StringBuilder b = new StringBuilder();
        if (paraTypes != null) {
            for (TypeName type : paraTypes) {
                b.append(getSimpleName(type));
            }
            return b.toString();
        }
        return "Universal";
    }

    private static @NotNull FieldSpec serialVersionUid() {
        return FieldSpec.builder(
            TypeName.LONG,
            "serialVersionUID",
            Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL
        )
            .initializer("$LL", new Random().nextLong())
            .build();
    }

    private static void codeConvertPara(
        CodeBlock.Builder builder,
        String paraName,
        TypeName required,
        TypeName actual
    ) {
        boolean converted = false;
        if (actual != null) {
            if (required.equals(TypeName.get(BigDecimal.class))) {
                if (actual.equals(TypeName.get(Double.class))
                    || actual.equals(TypeName.get(Long.class))
                    || actual.equals(TypeName.get(Integer.class))
                ) {
                    builder.add("BigDecimal.valueOf(($T) $L)", actual, paraName);
                    converted = true;
                }
            } else if (required.equals(TypeName.get(Double.class))) {
                if (actual.equals(TypeName.get(Long.class)) || actual.equals(TypeName.get(Integer.class))) {
                    builder.add("(($T) $L).doubleValue()", actual, paraName);
                    converted = true;
                }
            } else if (required.equals(TypeName.get(Long.class))) {
                if (actual.equals(TypeName.get(Integer.class))) {
                    builder.add("(($T) $L).longValue()", actual, paraName);
                    converted = true;
                }
            }
        }
        if (!converted) {
            builder.add("($T) $L", required, paraName);
        }
    }

    private static void codeConvertParas(
        CodeBlock.Builder codeBuilder,
        List<String> paraNames,
        @NotNull List<TypeName> paras,
        List<TypeName> newParas
    ) {
        boolean addComma = false;
        for (int i = 0; i < paras.size(); i++) {
            if (addComma) {
                codeBuilder.add(", ");
            }
            codeConvertPara(codeBuilder, paraNames.get(i), paras.get(i), newParas.get(i));
            addComma = true;
        }
    }

    private static @Nullable ExecutableElement getMethodByNameAndParaTypes(
        @NotNull TypeElement element,
        String name,
        List<TypeName> paraTypes
    ) {
        List<ExecutableElement> methods = ElementFilter.methodsIn(element.getEnclosedElements());
        for (ExecutableElement m : methods) {
            if (m.getSimpleName().toString().equals(name)) {
                if (paraTypes == null) {
                    return m;
                }
                List<TypeName> types = m.getParameters().stream()
                    .map(p -> TypeName.get(p.asType()))
                    .collect(Collectors.toList());
                if (types.equals(paraTypes)) {
                    return m;
                }
            }
        }
        return null;
    }

    private static ExecutableElement getMethodByName(
        TypeElement element,
        String name
    ) {
        return getMethodByNameAndParaTypes(element, name, null);
    }

    private @Nullable AnnotationMirror getAnnotationMirror(Element element, Class<?> annotationClass) {
        for (AnnotationMirror am : processingEnv.getElementUtils().getAllAnnotationMirrors(element)) {
            if (am.getAnnotationType().toString().equals(annotationClass.getName())) {
                return am;
            }
        }
        return null;
    }

    // Helper to get annotation value of type `Class<?>`
    private @Nullable AnnotationValue getAnnotationValue(AnnotationMirror annotationMirror, String methodName) {
        if (annotationMirror != null) {
            for (Map.Entry<? extends ExecutableElement, ? extends AnnotationValue> entry
                : annotationMirror.getElementValues().entrySet()) {
                if (entry.getKey().getSimpleName().toString().equals(methodName)) {
                    return entry.getValue();
                }
            }
        }
        return null;
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return ImmutableSet.<String>builder()
            .add(Evaluators.class.getName())
            .build();
    }

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
    }

    private @Nullable TypeElement getTypeElementFromAnnotation(
        AnnotationMirror annotationMirror,
        String methodName
    ) {
        AnnotationValue value = getAnnotationValue(annotationMirror, methodName);
        if (value == null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "Null value is not allowed for \"" + methodName + "\" field.");
            return null;
        }
        String elementName = value.getValue().toString();
        TypeElement element = processingEnv.getElementUtils().getTypeElement(elementName);
        if (element == null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "Cannot find a class of name \"" + elementName + "\".");
        }
        return element;
    }

    @Override
    public boolean process(@NotNull Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        for (TypeElement annotation : annotations) {
            if (annotation.getQualifiedName().contentEquals(Evaluators.class.getCanonicalName())) {
                Set<? extends Element> elements = roundEnv.getElementsAnnotatedWith(annotation);
                for (Element element : elements) {
                    AnnotationMirror annotationMirror = getAnnotationMirror(element, Evaluators.class);
                    TypeElement evaluatorInterface = getTypeElementFromAnnotation(
                        annotationMirror,
                        "evaluatorInterface"
                    );
                    TypeElement evaluatorFactory = getTypeElementFromAnnotation(
                        annotationMirror,
                        "evaluatorFactory"
                    );
                    TypeElement universalEvaluator = getTypeElementFromAnnotation(
                        annotationMirror,
                        "universalEvaluator"
                    );
                    if (evaluatorInterface == null
                        || evaluatorFactory == null
                        || universalEvaluator == null) {
                        continue;
                    }
                    EvaluatorInfo info = new EvaluatorInfo();
                    info.setEvaluatorInterface(evaluatorInterface);
                    info.setEvaluatorFactory(evaluatorFactory);
                    info.setUniversalEvaluator(universalEvaluator);
                    generateEvaluators(element, info);
                }
            }
        }
        return true;
    }

    private void saveSourceFile(String packageName, TypeSpec typeSpec) {
        JavaFile javaFile = JavaFile.builder(packageName, typeSpec)
            .indent("    ")
            .skipJavaLangImports(true)
            .build();
        try {
            javaFile.writeTo(processingEnv.getFiler());
        } catch (IOException e) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getLocalizedMessage());
        }
    }

    private void generateEvaluatorClassFile(
        @NotNull EvaluatorInfo info,
        String className,
        MethodSpec evalSpec
    ) {
        TypeSpec typeSpec = TypeSpec.classBuilder(className)
            .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
            .addSuperinterface(TypeName.get(info.getEvaluatorInterface().asType()))
            .addField(serialVersionUid())
            .addMethod(evalSpec)
            .build();
        String packageName = info.getPackageName();
        saveSourceFile(packageName, typeSpec);
        processingEnv.getMessager().printMessage(Diagnostic.Kind.NOTE,
            "Evaluator \"" + className + "\" generated in package \"" + packageName + "\".");
    }

    private void generateEvaluator(
        @NotNull ExecutableElement element,
        @NotNull EvaluatorInfo info,
        List<TypeName> newParas
    ) {
        String methodName = element.getSimpleName().toString();
        Map<String, TypeName> evaluatorMap = info.getEvaluatorMap()
            .computeIfAbsent(methodName, k -> new HashMap<>());
        List<TypeName> paras = getParaTypeList(element);
        if (newParas == null) {
            newParas = paras;
        }
        String evaluatorId = getEvaluatorId(newParas);
        if (evaluatorMap.containsKey(evaluatorId)) {
            return;
        }
        CodeBlock.Builder codeBuilder = CodeBlock.builder();
        codeBuilder.add("return $T.$L(", info.getOriginClassName(), methodName);
        ExecutableElement evalMethod = getMethodByName(info.getEvaluatorInterface(), EVAL_METHOD_NAME);
        if (evalMethod == null) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "No \"" + EVAL_METHOD_NAME + "\" method found in class \"" + element.getSimpleName() + "\".");
            return;
        }
        List<String> paraNames = getParaNameList(evalMethod);
        codeConvertParas(codeBuilder, paraNames, paras, newParas);
        codeBuilder.add(");\n");
        TypeName returnType = getBoxedType(TypeName.get(element.getReturnType()));
        MethodSpec methodSpec = MethodSpec.overriding(Objects.requireNonNull(evalMethod))
            .returns(returnType)
            .addCode(codeBuilder.build())
            .build();
        generateEvaluatorClassFile(info, getClassName(methodName, evaluatorId), methodSpec);
        evaluatorMap.put(evaluatorId, returnType);
    }

    private void induceEvaluators(ExecutableElement element, EvaluatorInfo info) {
        List<TypeName> paras = getParaTypeList(element);
        induceEvaluatorsRecursive(element, info, paras, 0);
    }

    private void tryDescentType(
        ExecutableElement element,
        EvaluatorInfo info,
        @NotNull List<TypeName> newParas,
        int pos,
        TypeName newTypeName
    ) {
        TypeName oldType = newParas.get(pos);
        newParas.set(pos, newTypeName);
        induceEvaluatorsRecursive(element, info, newParas, pos + 1);
        newParas.set(pos, oldType);
    }

    private void induceEvaluatorsRecursive(
        ExecutableElement element,
        EvaluatorInfo info,
        @NotNull List<TypeName> newParas,
        int pos
    ) {
        if (pos >= newParas.size()) {
            generateEvaluator(element, info, newParas);
            return;
        }
        induceEvaluatorsRecursive(element, info, newParas, pos + 1);
        TypeName type = getParaTypeList(element).get(pos);
        if (type.equals(TypeName.get(BigDecimal.class))) {
            tryDescentType(element, info, newParas, pos, TypeName.get(Double.class));
            tryDescentType(element, info, newParas, pos, TypeName.get(Long.class));
            tryDescentType(element, info, newParas, pos, TypeName.get(Integer.class));
        } else if (type.equals(TypeName.get(Double.class))) {
            tryDescentType(element, info, newParas, pos, TypeName.get(Long.class));
            tryDescentType(element, info, newParas, pos, TypeName.get(Integer.class));
        } else if (type.equals(TypeName.get(Long.class))) {
            tryDescentType(element, info, newParas, pos, TypeName.get(Integer.class));
        }
    }

    private void generateEvaluatorFactories(@NotNull EvaluatorInfo info) {
        String packageName = info.getPackageName();
        TypeElement evaluatorFactory = info.getEvaluatorFactory();
        TypeElement universalEvaluator = info.getUniversalEvaluator();
        Map<String, Map<String, TypeName>> multiEvaluatorMap = info.getEvaluatorMap();
        for (String m : multiEvaluatorMap.keySet()) {
            ClassName className = ClassName.get(
                packageName,
                getClassName(m, "EvaluatorFactory")
            );
            Map<String, TypeName> evaluatorMap = multiEvaluatorMap.get(m);
            CodeBlock.Builder initBuilder = CodeBlock.builder();
            for (Map.Entry<String, TypeName> entry : evaluatorMap.entrySet()) {
                initBuilder
                    .addStatement("$L.put($S, new $T())",
                        LOOKUP_VAR_NAME, entry.getKey(), ClassName.get(packageName, getClassName(m, entry.getKey()))
                    )
                    .addStatement("$L.put($S, $T.class)",
                        TYPES_VAR_NAME, entry.getKey(), entry.getValue()
                    );
            }
            String universalId = getEvaluatorId(null);
            initBuilder
                .addStatement("$L.put($S, new $T(this))",
                    LOOKUP_VAR_NAME, universalId, ClassName.get(universalEvaluator)
                )
                .addStatement("$L.put($S, $T.class)",
                    TYPES_VAR_NAME, universalId, ClassName.OBJECT);
            TypeSpec typeSpec = TypeSpec.classBuilder(className)
                .superclass(TypeName.get(evaluatorFactory.asType()))
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addField(serialVersionUid())
                .addField(
                    FieldSpec.builder(className, INSTANCE_VAR_NAME, Modifier.PUBLIC, Modifier.STATIC)
                        .initializer("new $T()", className)
                        .build()
                )
                .addMethod(
                    MethodSpec.constructorBuilder()
                        .addModifiers(Modifier.PRIVATE)
                        .addCode(initBuilder.build())
                        .build()
                )
                .build();
            saveSourceFile(packageName, typeSpec);
        }
    }

    private void generateEvaluators(@NotNull Element element, EvaluatorInfo info) {
        Element pkg = element.getEnclosingElement();
        if (pkg.getKind() != ElementKind.PACKAGE) {
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR,
                "Class annotated with \"Evaluators\" must not be an inner class.");
        }
        info.setPackageName(pkg.asType().toString() + "." + getSubPackageName(element));
        info.setOriginClassName(TypeName.get(element.asType()));
        info.setEvaluatorMap(new HashMap<>());
        List<ExecutableElement> executableElements = ElementFilter.methodsIn(element.getEnclosedElements());
        executableElements.forEach(e -> generateEvaluator(e, info, null));
        executableElements.forEach(e -> induceEvaluators(e, info));
        generateEvaluatorFactories(info);
    }

    @Data
    public static class EvaluatorInfo {
        private String packageName;
        private TypeName originClassName;
        private TypeElement evaluatorInterface;
        private TypeElement evaluatorFactory;
        private TypeElement universalEvaluator;
        private Map<String, Map<String, TypeName>> evaluatorMap;
    }
}
