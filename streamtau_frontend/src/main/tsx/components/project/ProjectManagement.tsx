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
import { autobind } from "core-decorators";

import { Profile, ProfileApi } from "../../api/ProfileApi";
import { ProjectApi, Project } from "../../api/ProjectApi";
import { checkStatusHandler } from "../../api/Api";
import { MainFrame } from "../MainFrame";
import { ProjectDialog } from "./ProjectDialog";
import { ProjectList } from "./ProjectList";

import Button from "@material-ui/core/Button";

interface ProjectManagementProps {
    parent: MainFrame;
}

export class ProjectManagement extends React.Component<ProjectManagementProps, {}> {
    private list: React.RefObject<ProjectList> = React.createRef();
    private dlg: React.RefObject<ProjectDialog> = React.createRef();

    private profile: Profile | undefined;

    public constructor(props: ProjectManagementProps) {
        super(props);
    }

    @autobind
    private doWithProfile(fun: (p: Profile) => void): void {
        if (this.profile) {
            fun(this.profile);
        } else {
            ProfileApi.profile("Project", checkStatusHandler(data => {
                this.profile = data;
                if (this.profile) {
                    fun(this.profile);
                }
            }));
        }
    }

    @autobind
    private handleCreateProject(): void {
        const project = {
            name: '',
            description: '',
            type: 'CONTAINER',
        };
        this.doWithProfile(p => this.dlg.current?.open(p, project));
    }

    @autobind
    public handleUpdateProject(id: string): void {
        const project = this.list.current?.state.projects[id];
        if (project) {
            this.doWithProfile(p => this.dlg.current?.open(p, project, id));
        } else {
            alert('No project with (id = "' + id + '") exists.');
        }
    }

    @autobind
    public handleDeleteProject(id: string): void {
        const ans = confirm('Are you sure to delete project ' + id + '?');
        if (ans) {
            this.deleteProject(id);
        }
    }

    @autobind
    public handleOpenProject(id: string): void {
        this.props.parent.handleOpenAssetManagement(id);
    }

    @autobind
    public listProject(): void {
        ProjectApi.listAll(checkStatusHandler(data => {
            this.list.current?.setProjects(data);
        }));
    }

    @autobind
    public createProject(req: Project): void {
        ProjectApi.create(req, checkStatusHandler(_data => {
            this.listProject();
        }));
    }

    @autobind
    public updateProject(id: string, req: Project): void {
        ProjectApi.update(id, req, checkStatusHandler(_data => {
            this.listProject();
        }));
    }

    @autobind
    public deleteProject(id: string): void {
        ProjectApi.delete(id, checkStatusHandler(_data => {
            this.listProject();
        }));
    }

    public componentDidMount(): void {
        this.listProject();
    }

    public render() {
        return (
            <React.Fragment>
                <Button
                    variant="outlined"
                    color="primary"
                    onClick={this.handleCreateProject}
                > Create project </Button>
                <ProjectList parent={this} ref={this.list} />
                <ProjectDialog parent={this} ref={this.dlg} />
            </React.Fragment>
        );
    }
}
