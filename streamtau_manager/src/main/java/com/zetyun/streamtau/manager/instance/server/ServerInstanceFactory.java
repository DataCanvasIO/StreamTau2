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

package com.zetyun.streamtau.manager.instance.server;

import com.zetyun.streamtau.manager.exception.StreamTauException;
import com.zetyun.streamtau.manager.pea.server.Server;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.function.Function;

@Slf4j
public class ServerInstanceFactory {
    private static ServerInstanceFactory INS;

    private final Map<String, Function<Server, ServerInstance>> instanceSupplierMap;

    private ServerInstanceFactory() {
        instanceSupplierMap = new LinkedHashMap<>(10);
        registerStarter("Executor", ExecutorInstance::new);
    }

    public static ServerInstanceFactory get() {
        if (INS == null) {
            INS = new ServerInstanceFactory();
        }
        return INS;
    }

    private void registerStarter(String type, Function<Server, ServerInstance> supplier) {
        instanceSupplierMap.put(type, supplier);
    }

    @SuppressWarnings("unchecked")
    public <S extends ServerInstance> S getServerInstance(Server server) {
        String type = server.getType();
        Function<Server, ServerInstance> supplier = instanceSupplierMap.get(type);
        if (supplier == null) {
            throw new StreamTauException("10103", type);
        }
        S serverInstance = (S) supplier.apply(server);
        serverInstance.checkStatus();
        return serverInstance;
    }
}
