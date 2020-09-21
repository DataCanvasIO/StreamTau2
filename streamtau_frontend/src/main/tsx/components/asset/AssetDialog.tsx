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
import { JSONSchema7 } from 'json-schema';
import MuiForm from "@rjsf/material-ui";

import { Asset } from '../../api/AssetApi';
import { AssetManagement } from './AssetManagement';

import Button from '@material-ui/core/Button';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import { ISubmitEvent } from '@rjsf/core';

interface AssetDialogProps {
    parent: AssetManagement;
}

interface AssetDialogState {
    isOpen: boolean;
    id?: string;
    type: string;
    schema: JSONSchema7;
    data?: Asset;
}

export class AssetDialog extends React.Component<AssetDialogProps, AssetDialogState> {
    public constructor(props: AssetDialogProps) {
        super(props);
        const type = this.props.parent.getSelectedType();
        this.state = {
            isOpen: false,
            type: type,
            schema: this.props.parent.getSchemaOfType(type),
        };
    }

    @autobind
    public setSelectedType(type: string): void {
        this.setState({
            type: type,
            schema: this.props.parent.getSchemaOfType(type),
        });
    }

    @autobind
    public open(id?: string): void {
        if (id) {
            const asset = this.props.parent.getCachedAsset(id);
            if (asset) {
                this.setState({
                    isOpen: true,
                    id: id,
                    data: asset,
                    schema: this.props.parent.getSchemaOfType(asset.type),
                });
            } else {
                alert('No asset with (id = "' + id + '") exists.');
                return;
            }
        } else {
            const type = this.state.type;
            const schema = this.props.parent.getSchemaOfType(type);
            if (!type || !schema) {
                alert("Schema of asset is not known. Please select an Asset Type first.");
                return;
            }
            this.setState({
                isOpen: true,
                id: undefined,
                data: {
                    name: '',
                    description: '',
                    type: type,
                    schema: schema,
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
        const asset = event.formData;
        if (this.state.id) {
            this.props.parent.updateAsset(this.state.id, asset);
        } else {
            this.props.parent.createAsset(asset);
        }
        this.handleClose();
    }

    public render() {
        return (
            <Dialog disableBackdropClick open={this.state.isOpen} onClose={this.handleClose}>
                <DialogTitle>Create {this.state.type}</DialogTitle>
                <DialogContent>
                    <MuiForm
                        schema={this.state.schema}
                        formData={this.state.data}
                        onSubmit={this.handleSubmit}
                    >
                        <DialogActions>
                            <Button onClick={this.handleClose}>Cancel</Button>
                            <Button type="submit" color="primary">Submit</Button>
                        </DialogActions>
                    </MuiForm>
                </DialogContent>
            </Dialog>
        );
    }
}
