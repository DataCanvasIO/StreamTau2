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
import { autobind } from "core-decorators";

import { ResponseHandler } from "./Api";
import { ProjectApi } from "./ProjectApi";

export interface Asset {
    name: string;
    description?: string;
    type: string;
    [props: string]: any;
}

export class AssetApi {
    private readonly urlBase: string;

    public constructor(projectId: string) {
        this.urlBase = ProjectApi.URL_BASE + '/' + projectId + '/assets';
    }

    @autobind
    public listAllAsset(callback: ResponseHandler): void {
        request
            .get(this.urlBase)
            .send()
            .end(callback);
    }

    @autobind
    public listAssetByType(type: string, callback: ResponseHandler): void {
        request
            .get(this.urlBase)
            .query({ type: type })
            .send()
            .end(callback);
    }

    @autobind
    public createAsset(req: Asset, callback: ResponseHandler): void {
        request
            .post(this.urlBase)
            .send(req)
            .end(callback);
    }

    @autobind
    public updateAsset(id: string, req: Asset, callback: ResponseHandler): void {
        request
            .put(this.urlBase + '/' + id)
            .send(req)
            .end(callback);
    }

    @autobind
    public deleteAsset(id: string, callback: ResponseHandler): void {
        request
            .delete(this.urlBase + '/' + id)
            .send()
            .end(callback);
    }

    @autobind
    public listCategory(callback: ResponseHandler): void {
        request
            .get(this.urlBase + '/types')
            .send()
            .end(callback);
    }
}
