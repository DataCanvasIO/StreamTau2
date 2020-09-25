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

import { ResponseHandler, API_URL_BASE } from "./Api";

export interface Project {
    name: string;
    description?: string;
    type: string;
}

export class ProjectApi {
    public static readonly URL_BASE = API_URL_BASE + '/projects';

    public static listAll(callback: ResponseHandler): void {
        request
            .get(ProjectApi.URL_BASE)
            .send()
            .end(callback);
    }

    public static create(req: Project, callback: ResponseHandler): void {
        request
            .post(ProjectApi.URL_BASE)
            .send(req)
            .end(callback);
    }

    public static update(id: string, req: Project, callback: ResponseHandler): void {
        request
            .put(ProjectApi.URL_BASE + '/' + id)
            .send(req)
            .end(callback);
    }

    public static delete(id: string, callback: ResponseHandler): void {
        request
            .delete(ProjectApi.URL_BASE + '/' + id)
            .send()
            .end(callback);
    }
}
