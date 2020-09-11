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

export const API_URL_BASE = 'http://localhost:13630/api';

export type ResponseHandler = (err: any, res: request.Response) => void;

export function checkStatusHandler(callback: (data: any) => void): ResponseHandler {
    return (err, res) => {
        console.log("err = ", err, ", res = ", res);
        if (res.body.status == '0') {
            callback(res.body.data);
        } else {
            alert(res.body.message);
        }
    }
}
