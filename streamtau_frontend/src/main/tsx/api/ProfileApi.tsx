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

import * as request from "superagent";
import { JSONSchema7 } from 'json-schema';

import { API_URL_BASE, ResponseHandler } from "./Api";
import { ProjectApi } from "./ProjectApi";

export interface Profile {
    schema: JSONSchema7;
    refs?: { [key: string]: string };
}

export class ProfileApi {
    public static profile(name: string, callback: ResponseHandler): void {
        request
            .get(API_URL_BASE + '/profile/' + name)
            .send()
            .end(callback);
    }

    public static profileInProject(projectId: string, name: string, callback: ResponseHandler): void {
        request
            .get(ProjectApi.URL_BASE + '/' + projectId + '/profile/' + name)
            .send()
            .end(callback);
    }

    public static listAssetTypes(callback: ResponseHandler): void {
        request
            .get(API_URL_BASE + '/assetTypes')
            .send()
            .end(callback);
    }
}
