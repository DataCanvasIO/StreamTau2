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

import * as React from "react";

import { ProjectDialog } from "./ProjectDialog";
import { ProjectList } from "./ProjectList";

import * as superagent from "superagent";
import { autobind } from "core-decorators";
import Button from "@material-ui/core/Button";
import Paper from "@material-ui/core/Paper";

interface ProjectManagementProps {
    urlBase: string;
}

export interface Project {
    name: string;
    description: string;
    type: string;
}

export class ProjectManagement extends React.Component<ProjectManagementProps, {}> {
    private static readonly pathPrefix = '/projects';
    private urlBase: string;

    private list: React.RefObject<ProjectList> = React.createRef();
    private dlg: React.RefObject<ProjectDialog> = React.createRef();

    public constructor(props: ProjectManagementProps) {
        super(props);
        this.urlBase = this.props.urlBase + ProjectManagement.pathPrefix;
    }

    public componentDidMount(): void {
        this.listProject();
    }

    @autobind
    private handleCreateProject(): void {
        this.dlg.current?.open();
    }

    @autobind
    public handleUpdateProject(id: string): void {
        this.dlg.current?.open(id);
    }

    @autobind
    public handleDeleteProject(id: string): void {
        const ans = confirm('Are you sure to delete project ' + id + '?');
        if (ans) {
            this.deleteProject(id);
        }
    }

    @autobind
    private checkStatusDo(err: any, res: superagent.Response, callback: (data: any) => void): void {
        console.log("err = ", err, ", res = ", res);
        if (res.body.status == '0') {
            callback(res.body.data);
        } else {
            alert(res.body.message);
        }
    }

    @autobind
    public makeProject(data: any): Project {
        return {
            name: data.name,
            description: data.description,
            type: data.type,
        }
    }

    @autobind
    public getCachedProject(id: string): Project | undefined {
        return this.list.current?.state[id];
    }

    @autobind
    public listProject(): void {
        superagent
            .get(this.urlBase)
            .send()
            .end((err, res) => this.checkStatusDo(err, res, (data) => {
                for (const rec of data) {
                    this.list.current?.setState({
                        [rec.id]: this.makeProject(rec)
                    });
                }
            }));
    }

    @autobind
    public createProject(req: Project): void {
        superagent
            .post(this.urlBase)
            .send(req)
            .end((err, res) => this.checkStatusDo(err, res, (data) => {
                this.list.current?.setState({
                    [data.id]: this.makeProject(data)
                });
            }));
    }

    @autobind
    public updateProject(id: string, req: Project): void {
        superagent
            .put(this.urlBase + '/' + id)
            .send(req)
            .end((err, res) => this.checkStatusDo(err, res, (data) => {
                this.list.current?.setState({
                    [data.id]: this.makeProject(data)
                });
            }));
    }

    @autobind
    public deleteProject(id: string): void {
        superagent
            .delete(this.urlBase + '/' + id)
            .send()
            .end((err, res) => this.checkStatusDo(err, res, (_data) => {
                this.list.current?.setState({
                    [id]: undefined
                });
            }));
    }

    public render() {
        return (
            <Paper>
                <Button
                    variant="outlined"
                    color="primary"
                    onClick={this.handleCreateProject}
                > Create project </Button>
                <ProjectList parent={this} ref={this.list} />
                <ProjectDialog parent={this} ref={this.dlg} />
            </Paper>
        );
    }
}
