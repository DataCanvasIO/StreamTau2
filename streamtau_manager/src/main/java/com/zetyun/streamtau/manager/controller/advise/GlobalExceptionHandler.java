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
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Locale;
import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestControllerAdvice(basePackages = {"com.zetyun.streamtau.manager.controller"})
public class GlobalExceptionHandler {
    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private static ResourceBundle errorMessages = null;

    private static ApiResponse getApiResponse(String errorCode, Object[] args) {
        loadMeassages();
        String message;
        try {
            message = String.format(errorMessages.getString(errorCode), args);
        } catch (MissingResourceException e) {
            message = "Unknown error!";
        }
        return new ApiResponse(errorCode, message);
    }

    private static void loadMeassages() {
        if (errorMessages == null || !errorMessages.getLocale().equals(Locale.getDefault())) {
            errorMessages = ResourceBundle.getBundle("messages/error");
        }
    }

    @ExceptionHandler(value = {HttpMessageNotReadableException.class})
    public ApiResponse requestExceptionHandler(
        HttpServletRequest request,
        Exception exception,
        HttpServletResponse response
    ) {
        logger.error("Exception thrown: ", exception);
        loadMeassages();
        return getApiResponse("10201", null);
    }

    @ExceptionHandler(value = {StreamTauException.class})
    public ApiResponse streamTauExceptionHandler(
        HttpServletRequest request,
        Exception exception,
        HttpServletResponse response
    ) {
        String errorCode = ((StreamTauException) exception).getErrorCode();
        Object[] args = ((StreamTauException) exception).getArgs();
        return getApiResponse(errorCode, args);
    }

    @ExceptionHandler(value = {Exception.class})
    public ApiResponse globalExceptionHandler(
        HttpServletRequest request,
        Exception exception,
        HttpServletResponse response
    ) {
        logger.error("Exception thrown: ", exception);
        return getApiResponse("100000", null);
    }
}
