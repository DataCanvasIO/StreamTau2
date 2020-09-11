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

import { AssetManagement } from './AssetManagement';

import Button from '@material-ui/core/Button';
import TextField from '@material-ui/core/TextField';
import Dialog from '@material-ui/core/Dialog';
import DialogActions from '@material-ui/core/DialogActions';
import DialogContent from '@material-ui/core/DialogContent';
import DialogTitle from '@material-ui/core/DialogTitle';
import FormLabel from '@material-ui/core/FormLabel';

interface AssetDialogProps {
    parent: AssetManagement;
}

interface AssetDialogState {
    isOpen: boolean;
    id?: string;
    name: string;
    description?: string;
    type: string;
    [props: string]: any;
}

export class AssetDialog extends React.Component<AssetDialogProps, AssetDialogState> {
    public constructor(props: AssetDialogProps) {
        super(props);
        this.state = {
            isOpen: false,
            name: '',
            type: props.parent.getSelectedType(),
        };
    }

    @autobind
    public open(id?: string): void {
        if (id) {
            const asset = this.props.parent.getCachedAsset(id);
            if (asset && asset.type === this.state.type) {
                this.setState({
                    isOpen: true,
                    id: id,
                    name: asset.name,
                    description: asset.description,
                });
            } else {
                alert('No asset of type "' + this.state.type + '" with (id = "' + id + '") exists.');
                return;
            }
        } else {
            this.setState({
                isOpen: true,
                id: undefined,
                name: '',
                description: '',
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
    private handleSubmit(event: React.FormEvent<HTMLFormElement>): void {
        event.preventDefault();
        const asset = {
            name: this.state.name,
            description: this.state.description,
            type: this.state.type,
        };
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
