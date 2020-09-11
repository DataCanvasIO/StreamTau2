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

import { ProjectManagement } from './project/ProjectManagement';
import { AssetManagement } from './asset/AssetManagement';

import Box from '@material-ui/core/Box';
import Paper from '@material-ui/core/Paper';
import AppBar from '@material-ui/core/AppBar';
import Toolbar from '@material-ui/core/Toolbar';
import Typography from '@material-ui/core/Typography';

enum Management {
    ManageProjects,
    ManageAssets,
}

interface MainFrameProps {
}

interface MainFrameState {
    management: Management;
    projectId?: string;
}

export class MainFrame extends React.Component<MainFrameProps, MainFrameState> {
    public constructor(props: MainFrameProps) {
        super(props);
        this.state = {
            management: Management.ManageProjects,
        };
    }

    @autobind
    public handleOpenAssetManagement(id: string): void {
        this.setState({
            management: Management.ManageAssets,
            projectId: id,
        });
    }

    @autobind
    public handleOpenProjectManagement(): void {
        this.setState({
            management: Management.ManageProjects,
        });
    }

    @autobind
    private getMainPage() {
        const management = this.state.management;
        if (management === Management.ManageProjects) {
            return <ProjectManagement parent={this} />
        } else if (management == Management.ManageAssets) {
            if (this.state.projectId) {
                return <AssetManagement parent={this} projectId={this.state.projectId} />
            }
        }
        return <Box />;
    }

    public render() {
        return (
            <Paper>
                <AppBar position="sticky">
                    <Toolbar>
                        <Typography variant="h3">StreamTau</Typography>
                    </Toolbar>
                </AppBar>
                {this.getMainPage()}
            </Paper>
        );
    }
}
