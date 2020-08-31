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

package com.zetyun.streamtau.manager.controller.advise;

import com.zetyun.streamtau.manager.exception.StreamTauException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice(basePackages = {"com.zetyun.streamtau.manager.controller"})
@Slf4j
public class GlobalExceptionHandler {
    private static ResourceBundle errorMessages = null;

    @Nonnull
    private static StreamTauResponse getApiResponse(String errorCode, @Nullable Object[] args) {
        loadMeassages();
        String message;
        try {
            message = String.format(errorMessages.getString(errorCode), args);
        } catch (MissingResourceException e) {
            message = "Unknown error!";
        }
        return new StreamTauResponse(errorCode, message);
    }

    private static void loadMeassages() {
        if (errorMessages == null || !errorMessages.getLocale().equals(Locale.getDefault())) {
            errorMessages = ResourceBundle.getBundle("messages/error");
        }
    }

    @ExceptionHandler(value = {MethodArgumentNotValidException.class})
    public StreamTauResponse validationExceptionHandler(
        HttpServletRequest request,
        Exception exception,
        HttpServletResponse response
    ) {
        if (log.isWarnEnabled()) {
            log.warn("Exception thrown: ", exception);
        }
        // TODO: more friendly messages.
        BindingResult result = ((MethodArgumentNotValidException) exception).getBindingResult();
        for (ObjectError error : result.getAllErrors()) {
            if (log.isWarnEnabled()) {
                log.warn("Error: {}", error);
            }
        }
        return getApiResponse("10201", null);
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public StreamTauResponse requestExceptionHandler(
        HttpServletRequest request,
        Exception exception,
        HttpServletResponse response
    ) {
        if (log.isErrorEnabled()) {
            log.error("Exception thrown: ", exception);
        }
        return getApiResponse("10201", null);
    }

    @ExceptionHandler(value = {StreamTauException.class})
    public StreamTauResponse streamTauExceptionHandler(
        HttpServletRequest request,
        Exception exception,
        HttpServletResponse response
    ) {
        if (log.isErrorEnabled()) {
            log.error("Exception thrown: ", exception);
        }
        String errorCode = ((StreamTauException) exception).getErrorCode();
        Object[] args = ((StreamTauException) exception).getArgs();
        return getApiResponse(errorCode, args);
    }

    @ExceptionHandler(value = {Exception.class})
    public StreamTauResponse globalExceptionHandler(
        HttpServletRequest request,
        Exception exception,
        HttpServletResponse response
    ) {
        if (log.isErrorEnabled()) {
            log.error("Exception thrown: ", exception);
        }
        return getApiResponse("100000", null);
    }
}
