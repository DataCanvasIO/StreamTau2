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

import styles from 'style/main.scss';

import * as React from 'react';
import { autobind } from 'core-decorators';

import { AssetManagement } from './AssetManagement';

import List from '@material-ui/core/List';
import ListItem from '@material-ui/core/ListItem';
import ListItemText from '@material-ui/core/ListItemText';
import Collapse from '@material-ui/core/Collapse';
import ExpandLess from '@material-ui/icons/ExpandLess';
import ExpandMore from '@material-ui/icons/ExpandMore';

interface Categories {
    [category: string]: {
        isOpen: boolean;
        types: string[];
    }
}

interface AssetCategoryProps {
    parent: AssetManagement;
}

interface AssetCategoryState {
    categories: Categories;
    selectedType?: string;
}

export class AssetCategory extends React.Component<AssetCategoryProps, AssetCategoryState> {
    public constructor(props: AssetCategoryProps) {
        super(props);
        this.state = {
            categories: {},
        };
    }

    @autobind
    public setCategories(data: any): void {
        const categories: Categories = {};
        for (const item of data) {
            const type = item.type;
            const category = item.category;
            if (!categories[category]) {
                categories[category] = { isOpen: false, types: [] };
            }
            categories[category].types.push(type);
        }
        this.setState({
            categories: categories,
        });
    }

    @autobind
    private handleCategoryClick(category: string): void {
        this.setState((state, _props) => {
            const categories = state.categories;
            categories[category].isOpen = !categories[category].isOpen;
            return { categories: categories };
        });
    }

    @autobind
    private handleTypeClick(type: string): void {
        this.setState({ selectedType: type });
        this.props.parent.handleChangeSelectedType(type);
    }

    public render() {
        const list = [];
        for (const category in this.state.categories) {
            const categoryProps = this.state.categories[category];
            const isOpen = categoryProps.isOpen;
            const types = categoryProps.types;
            list.push(
                <ListItem key={category} button onClick={() => this.handleCategoryClick(category)}>
                    <ListItemText>{category}</ListItemText>
                    {isOpen ? <ExpandLess /> : <ExpandMore />}
                </ListItem>
            );
            list.push(
                <Collapse key={category + '-children'} in={isOpen} timeout="auto" unmountOnExit>
                    <List component="div" disablePadding className={styles['nested-list']}>
                        {types.map((type) => (
                            <ListItem
                                key={type}
                                button
                                onClick={() => this.handleTypeClick(type)}
                                className={type == this.state.selectedType ? styles['selected'] : undefined}
                            >
                                <ListItemText>{type}</ListItemText>
                            </ListItem>
                        ))}
                    </List>
                </Collapse>
            );
        }
        return (
            <List component="nav">
                {list}
            </List>
        );
    }
}
