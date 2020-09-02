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

import { ProjectManagement } from './ProjectManagement';

import { autobind } from 'core-decorators';
import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import MenuItem from '@material-ui/core/MenuItem';
import Select from '@material-ui/core/Select';
import FormLabel from '@material-ui/core/FormLabel';

interface ProjectDialogProps {
    parent: ProjectManagement;
}

interface ProjectDialogState {
    isOpen: boolean;
    id?: string;
    name: string;
    description: string;
    type: string;
}

export class ProjectDialog extends React.Component<ProjectDialogProps, ProjectDialogState> {
    private static initialState = {
        isOpen: false,
        id: undefined,
        name: '',
        description: '',
        type: 'CONTAINER',
    };

    public constructor(props: ProjectDialogProps) {
        super(props);
        this.state = ProjectDialog.initialState;
    }

    @autobind
    public open(id?: string): void {
        if (id) {
            const project = this.props.parent.getCachedProject(id);
            if (project) {
                this.setState({
                    id: id,
                    name: project.name,
                    description: project.description,
                    type: project.type,
                });
            } else {
                alert('No project with (id = "' + id + '") exists.');
                return;
            }
        } else {
            this.setState(ProjectDialog.initialState);
        }
        this.setState({
            isOpen: true,
        });
    }

    @autobind
    private handleClose(): void {
        this.setState({
            isOpen: false
        });
    }

    @autobind
    private handleInputChange(event: React.ChangeEvent<HTMLInputElement>): void {
        event.preventDefault();
        event.persist();
        const target = event.target;
        if (target != null) {
            const name = target.name;
            const value = target.value;
            if (value != null) {
                if (name == 'name') {
                    this.setState({ name: value });
                } else if (name == 'description') {
                    this.setState({ description: value });
                }
            }
        }
    }

    @autobind
    private handleSelectChange(
        event: React.ChangeEvent<{ name?: string | undefined, value: unknown }>,
        _child: React.ReactNode
    ): void {
        event.preventDefault();
        event.persist();
        const target = event.target;
        if (target != null) {
            const name = target.name;
            const value = target.value;
            if (name == 'type' && typeof value == 'string') {
                this.setState({ type: value });
            }
        }
    }

    @autobind
    private handleSubmit(event: React.FormEvent<HTMLFormElement>): void {
        event.preventDefault();
        const project = this.props.parent.makeProject(this.state);
        if (this.state.id) {
            this.props.parent.updateProject(this.state.id, project);
        } else {
            this.props.parent.createProject(project);
        }
        this.handleClose();
    }

    public render() {
        return (
            <Dialog disableBackdropClick open={this.state.isOpen} onClose={this.handleClose}>
                <DialogTitle>Create project</DialogTitle>
                <form onSubmit={this.handleSubmit} method="POST">
                    <DialogContent>
                        <FormLabel>Name</FormLabel>
                        <TextField autoFocus fullWidth
                            name="name"
                            value={this.state.name}
                            onChange={this.handleInputChange}
                        />
                        <FormLabel>Description</FormLabel>
                        <TextField fullWidth
                            name="description"
                            value={this.state.description}
                            onChange={this.handleInputChange}
                        />
                        <FormLabel>Type</FormLabel>
                        <Select fullWidth
                            name="type"
                            value={this.state.type}
                            onChange={this.handleSelectChange}
                        >
                            <MenuItem value="CONTAINER">CONTAINER</MenuItem>
                        </Select>
                    </DialogContent>
                    <DialogActions>
                        <Button onClick={this.handleClose}>Cancel</Button>
                        <Button type="submit" color="primary">Submit</Button>
                    </DialogActions>
                </form>
            </Dialog>
        );
    }
}
