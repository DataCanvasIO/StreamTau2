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

import { Profile, ProfileApi } from "../../api/ProfileApi";
import { checkStatusHandler } from "../../api/Api";
import { AssetApi, Asset } from "../../api/AssetApi";
import { MainFrame } from "../MainFrame";
import { AssetTypesTree } from "./AssetTypesTree";
import { AssetDialog } from "./AssetDialog";
import { AssetList } from "./AssetList";

import Button from "@material-ui/core/Button";
import Drawer from "@material-ui/core/Drawer";
import Box from "@material-ui/core/Box";
import Toolbar from "@material-ui/core/Toolbar";
import { JobApi } from "../../api/JobApi";

interface AssetManagementProps {
    parent: MainFrame;
    projectId: string;
}

interface AssetManagementState {
    selectedType: string;
}

export class AssetManagement extends React.Component<AssetManagementProps, AssetManagementState> {
    private cats: React.RefObject<AssetTypesTree> = React.createRef();
    private list: React.RefObject<AssetList> = React.createRef();
    private dlg: React.RefObject<AssetDialog> = React.createRef();

    public constructor(props: AssetManagementProps) {
        super(props);
        this.state = {
            selectedType: '',
        }
    }

    @autobind
    private handleReturnToProject() {
        this.props.parent.handleOpenProjectManagement();
    }

    @autobind
    public handleChangeSelectedType(type: string): void {
        this.setState({ selectedType: type });
        this.listAsset(type);
    }

    @autobind
    private doWithProfile(type: string, fun: (p: Profile) => void): void {
        ProfileApi.profileInProject(this.props.projectId, type, checkStatusHandler(data => {
            fun(data);
        }));
    }

    @autobind
    private handleCreateAsset(): void {
        const type = this.state.selectedType;
        if (type) {
            const asset = {
                name: '',
                description: '',
                type: type,
            };
            this.doWithProfile(type, p => this.dlg.current?.open(type, p, asset));
        } else {
            alert("Profile of asset is not known. Please select an Asset Type first.");
        }
    }

    @autobind
    public handleUpdateAsset(id: string): void {
        const asset = this.list.current?.state.assets[id];
        if (asset) {
            const type = asset.type;
            this.doWithProfile(type, p => this.dlg.current?.open(type, p, asset, id));
        } else {
            alert('No asset with (id = "' + id + '") exists.');
        }
    }

    @autobind
    public handleDeleteAsset(id: string): void {
        const ans = confirm('Are you sure to delete asset ' + id + '?');
        if (ans) {
            this.deleteAsset(id);
        }
    }

    @autobind
    public handlePublishApp(id: string): void {
        const app = this.list.current?.state.assets[id];
        if (app) {
            JobApi.create(this.props.projectId, {
                name: app.name,
                appId: app.id,
                appType: app.type,
                status: 'READY',
            }, checkStatusHandler(_data => { }))
        } else {
            alert('No app with (id = "' + id + '") exists.');
        }
    }

    @autobind
    public listAssetTypes(): void {
        ProfileApi.listAssetTypes(checkStatusHandler(data => {
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
            AssetApi.listByType(this.props.projectId, type, handler);
        } else {
            AssetApi.listAll(this.props.projectId, handler);
        }
    }

    @autobind
    public createAsset(req: Asset): void {
        AssetApi.create(this.props.projectId, req, checkStatusHandler(_data => {
            this.listAsset(this.state.selectedType);
        }));
    }

    @autobind
    public updateAsset(id: string, req: Asset): void {
        AssetApi.update(this.props.projectId, id, req, checkStatusHandler(_data => {
            this.listAsset(this.state.selectedType);
        }));
    }

    @autobind
    public deleteAsset(id: string): void {
        AssetApi.delete(this.props.projectId, id, checkStatusHandler(_data => {
            this.listAsset(this.state.selectedType);
        }));
    }

    public componentDidMount(): void {
        this.listAssetTypes();
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
                        <AssetTypesTree parent={this} ref={this.cats} />
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
