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

import { Asset } from "../../api/AssetApi";
import { AssetManagement } from "./AssetManagement";

import Table from "@material-ui/core/Table";
import TableContainer from "@material-ui/core/TableContainer";
import TableHead from "@material-ui/core/TableHead";
import TableRow from "@material-ui/core/TableRow";
import TableCell from "@material-ui/core/TableCell";
import TableBody from "@material-ui/core/TableBody";
import Paper from "@material-ui/core/Paper";
import Button from "@material-ui/core/Button";
import DeleteIcon from "@material-ui/icons/Delete";
import EditIcon from "@material-ui/icons/Edit";

interface Assets {
    [id: string]: Asset;
}

interface AssetListProps {
    parent: AssetManagement;
}

interface AssetListState {
    assets: Assets;
}

export class AssetList extends React.Component<AssetListProps, AssetListState> {
    public constructor(props: AssetListProps) {
        super(props);
        this.state = { assets: {} };
    }

    @autobind
    public setAssets(data: any): void {
        const assets: Assets = {};
        for (const item of data) {
            assets[item.id] = item;
        }
        this.setState({ assets: assets });
    }

    @autobind
    private handleUpdate(_event: React.MouseEvent<Element>, id: string): void {
        this.props.parent.handleUpdateAsset(id);
    }

    @autobind
    private handleDelete(_event: React.MouseEvent<Element>, id: string): void {
        this.props.parent.handleDeleteAsset(id);
    }

    public render() {
        const tableRows = [];
        for (const id in this.state.assets) {
            const asset = this.state.assets[id];
            tableRows.push(
                <TableRow key={id}>
                    <TableCell component="th" scope="row">{id}</TableCell>
                    <TableCell>{asset.name}</TableCell>
                    <TableCell>{asset.description}</TableCell>
                    <TableCell>{asset.type}</TableCell>
                    <TableCell>
                        <Button
                            startIcon={<EditIcon />}
                            onClick={(event) => this.handleUpdate(event, id)}
                            disabled={id.startsWith('COMMON_')}
                        > Edit </Button>
                    </TableCell>
                    <TableCell>
                        <Button
                            startIcon={<DeleteIcon />}
                            onClick={(event) => this.handleDelete(event, id)}
                            disabled={id.startsWith('COMMON_')}
                        > Delete </Button>
                    </TableCell>
                </TableRow>
            );
        }
        return (
            <TableContainer component={Paper}>
                <Table>
                    <TableHead>
                        <TableRow>
                            <TableCell>id</TableCell>
                            <TableCell>Name</TableCell>
                            <TableCell>Description</TableCell>
                            <TableCell>Type</TableCell>
                            <TableCell colSpan={2}>Actions</TableCell>
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
