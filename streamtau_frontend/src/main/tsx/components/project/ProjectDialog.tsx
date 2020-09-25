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

import * as React from 'react';
import { autobind } from 'core-decorators';
import { ISubmitEvent } from '@rjsf/core';

import { Project } from '../../api/ProjectApi';
import { ProjectManagement } from './ProjectManagement';
import { Profile } from '../../api/ProfileApi';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import MuiForm from "@rjsf/material-ui";
import Box from '@material-ui/core/Box';

interface ProjectDialogProps {
    parent: ProjectManagement;
}

interface ProjectDialogState {
    isOpen: boolean;
    id?: string;
    profile?: Profile;
    data?: Project;
}

export class ProjectDialog extends React.Component<ProjectDialogProps, ProjectDialogState> {
    public constructor(props: ProjectDialogProps) {
        super(props);
        this.state = {
            isOpen: false,
        };
    }

    @autobind
    public setProfile(profile: Profile): void {
        this.setState({
            profile: profile,
        })
    }

    @autobind
    public open(id?: string): void {
        if (id) {
            const project = this.props.parent.getCachedProject(id);
            if (project) {
                this.setState({
                    isOpen: true,
                    id: id,
                    data: project,
                });
            } else {
                alert('No project with (id = "' + id + '") exists.');
                return;
            }
        } else {
            this.setState({
                isOpen: true,
                id: undefined,
                data: {
                    name: '',
                    description: '',
                    type: 'CONTAINER',
                }
            });
        }
    }

    @autobind
    private handleClose(): void {
        this.setState({
            isOpen: false
        });
    }

    @autobind
    private handleSubmit(event: ISubmitEvent<any>): void {
        const project = event.formData;
        if (this.state.id) {
            this.props.parent.updateProject(this.state.id, project);
        } else {
            this.props.parent.createProject(project);
        }
        this.handleClose();
    }

    public componentDidMount(): void {
    }

    public render() {
        let dlgContent;
        if (this.state.profile) {
            dlgContent = (
                <MuiForm
                    schema={this.state.profile.schema}
                    formData={this.state.data}
                    onSubmit={this.handleSubmit}
                >
                    <DialogActions>
                        <Button type="submit" color="primary">Submit</Button>
                        <Button onClick={this.handleClose}>Cancel</Button>
                    </DialogActions>
                </MuiForm>
            );
        } else {
            dlgContent = (
                <Box />
            );
        }
        return (
            <Dialog disableBackdropClick open={this.state.isOpen} onClose={this.handleClose}>
                <DialogTitle>{this.state.id ? 'Update' : 'Create'} project</DialogTitle>
                <DialogContent>{dlgContent}</DialogContent>
            </Dialog>
        );
    }
}
