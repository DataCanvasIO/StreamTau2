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

import styles from "style/main.scss";

import * as React from "react";
import { autobind } from "core-decorators";
import { JSONSchema7 } from 'json-schema';

import { SchemaApi } from "../../api/SchemaApi";
import { checkStatusHandler } from "../../api/Api";
import { AssetApi, Asset } from "../../api/AssetApi";
import { MainFrame } from "../MainFrame";
import { AssetCategory } from "./AssetCategory";
import { AssetDialog } from "./AssetDialog";
import { AssetList } from "./AssetList";

import Button from "@material-ui/core/Button";
import Drawer from "@material-ui/core/Drawer";
import Box from "@material-ui/core/Box";
import Toolbar from "@material-ui/core/Toolbar";

interface AssetManagementProps {
    parent: MainFrame;
    projectId: string;
}

interface AssetManagementState {
    selectedType: string;
}

export class AssetManagement extends React.Component<AssetManagementProps, AssetManagementState> {
    private cats: React.RefObject<AssetCategory> = React.createRef();
    private list: React.RefObject<AssetList> = React.createRef();
    private dlg: React.RefObject<AssetDialog> = React.createRef();

    private readonly assetApi: AssetApi;
    private schemas: { [type: string]: JSONSchema7 };

    public constructor(props: AssetManagementProps) {
        super(props);
        this.assetApi = new AssetApi(props.projectId);
        this.state = {
            selectedType: '',
        }
        this.schemas = {};
    }

    @autobind
    public getSelectedType(): string {
        return this.state.selectedType;
    }

    @autobind
    public getSchemaOfType(type: string): JSONSchema7 {
        return this.schemas[type];
    }

    @autobind
    private handleReturnToProject() {
        this.props.parent.handleOpenProjectManagement();
    }

    @autobind
    public handleChangeSelectedType(type: string): void {
        this.setState({ selectedType: type });
        this.dlg.current?.setSelectedType(type);
        this.listAsset(type);
    }

    @autobind
    private handleCreateAsset(): void {
        this.dlg.current?.open();
    }

    @autobind
    public handleUpdateAsset(id: string): void {
        this.dlg.current?.open(id);
    }

    @autobind
    public handleDeleteAsset(id: string): void {
        const ans = confirm('Are you sure to delete project ' + id + '?');
        if (ans) {
            this.deleteAsset(id);
        }
    }

    @autobind
    public getCachedAsset(id: string): Asset | undefined {
        return this.list.current?.state.assets[id];
    }

    @autobind
    public listCategory(): void {
        this.assetApi.listCategory(checkStatusHandler(data => {
            for (const item of data) {
                const type = item.type;
                SchemaApi.get(type, (_err, res) => {
                    this.schemas[type] = res.body;
                });
            }
            this.cats.current?.setCategories(data);
            this.handleChangeSelectedType('');
        }));
    }

    @autobind
    public listAsset(type: string | undefined): void {
        const handler = checkStatusHandler(data => {
            this.list.current?.setAssets(data);
        });
        if (type) {
            this.assetApi.listAssetByType(type, handler);
        } else {
            this.assetApi.listAllAsset(handler);
        }
    }

    @autobind
    public createAsset(req: Asset): void {
        this.assetApi.createAsset(req, checkStatusHandler(_data => {
            this.listAsset(this.getSelectedType());
        }));
    }

    @autobind
    public updateAsset(id: string, req: Asset): void {
        this.assetApi.updateAsset(id, req, checkStatusHandler(_data => {
            this.listAsset(this.getSelectedType());
        }));
    }

    @autobind
    public deleteAsset(id: string): void {
        this.assetApi.deleteAsset(id, checkStatusHandler(_data => {
            this.listAsset(this.getSelectedType());
        }));
    }

    public componentDidMount(): void {
        this.listCategory();
    }

    public render() {
        return (
            <React.Fragment>
                <Box className={styles['panel']}>
                    <Drawer
                        variant="permanent"
                        anchor="left"
                        className={styles['drawer']}
                        classes={{ paper: styles['drawer-paper'] }}
                    >
                        <Toolbar></Toolbar>
                        <AssetCategory parent={this} ref={this.cats} />
                    </Drawer>
                    <Box className={styles['asset']}>
                        <Button
                            variant="outlined"
                            onClick={this.handleReturnToProject}
                        > Return to project list </Button>
                        <Button
                            variant="outlined"
                            color="primary"
                            onClick={this.handleCreateAsset}
                        > Create {this.state.selectedType} </Button>
                        <AssetList parent={this} ref={this.list} />
                    </Box>
                </Box>
                <AssetDialog parent={this} ref={this.dlg} />
            </React.Fragment>
        );
    }
}
