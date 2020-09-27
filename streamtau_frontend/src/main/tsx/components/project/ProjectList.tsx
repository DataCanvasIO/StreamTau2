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
import { autobind } from 'core-decorators';

import { Project } from "../../api/ProjectApi";
import { ProjectManagement } from "./ProjectManagement";

import Table from "@material-ui/core/Table";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import Button from "@material-ui/core/Button";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";
import Link from "@material-ui/core/Link";
import Box from "@material-ui/core/Box";

interface Projects {
    [id: string]: Project;
}

interface ProjectListProps {
    parent: ProjectManagement;
}

interface ProjectListState {
    projects: Projects;
}

export class ProjectList extends React.Component<ProjectListProps, ProjectListState> {
    public constructor(props: ProjectListProps) {
        super(props);
        this.state = { projects: {} };
    }

    @autobind
    public setProjects(data: any): void {
        const projects: Projects = {};
        for (const item of data) {
            projects[item.id] = item;
        }
        this.setState({ projects: projects });
    }

    @autobind
    private handleUpdate(id: string): void {
        this.props.parent.handleUpdateProject(id);
    }

    @autobind
    private handleDelete(id: string): void {
        this.props.parent.handleDeleteProject(id);
    }

    @autobind
    private handleOpenProject(id: string): void {
        this.props.parent.handleOpenProject(id);
    }

    public render() {
        const tableRows = [];
        for (const id in this.state.projects) {
            const project = this.state.projects[id];
            tableRows.push(
                <TableRow key={id}>
                    <TableCell component="th" scope="row">
                        <Link href="#" onClick={() => this.handleOpenProject(id)}>{id}</Link>
                    </TableCell>
                    <TableCell>
                        <Link href="#" onClick={() => this.handleOpenProject(id)}>{project.name}</Link>
                    </TableCell>
                    <TableCell>{project.description}</TableCell>
                    <TableCell>{project.type}</TableCell>
                    <TableCell>
                        <Button
                            startIcon={<EditIcon />}
                            onClick={() => this.handleUpdate(id)}
                        > Edit </Button>
                        <Button
                            startIcon={<DeleteIcon />}
                            onClick={() => this.handleDelete(id)}
                        > Delete </Button>
                    </TableCell>
                </TableRow>
            );
        }
        return (
            <TableContainer component={Box}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>id</TableCell>
                            <TableCell>Name</TableCell>
                            <TableCell>Description</TableCell>
                            <TableCell>Type</TableCell>
                            <TableCell>Actions</TableCell>
                        </TableRow>
                    </TableHead>
                    <TableBody>
                        {tableRows}
                    </TableBody>
                </Table>
            </TableContainer>
        );
    }
}
