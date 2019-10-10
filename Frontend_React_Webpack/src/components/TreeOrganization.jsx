import axios from "axios";
import React, {PureComponent} from 'react';
import {Treebeard} from 'react-treebeard';

class TreeOrganization extends PureComponent{
    constructor(props) {
        super(props);
        this.state = {
            hits:[]
        };

        this.fetchURL = this.fetchURL.bind(this);
        this.onToggle = this.onToggle.bind(this);
    }

    fetchURL() {
        axios.get(`http://localhost:8080/organizations/tree`)
            .then(response =>{
                this.setState({hits: response.data});
                }
            );
    }

    componentDidMount(){
        this.fetchURL();
    }

    onToggle(node, toggled){
        const {cursor, data} = this.state;
        if (cursor) {
            this.setState(() => ({cursor, active: false}));
        }
        node.active = true;
        if (node.children) {
            node.toggled = toggled;
        }
        this.setState(() => ({cursor: node, data: Object.assign({}, data)}));
    }

    render(){
        const {hits} = this.state;

        return (
            <div>
                <div><h3>Organization Tree</h3></div>
                <div>
                    <Treebeard
                    data={hits}
                    onToggle={this.onToggle}
                    />
                </div>
            </div>
        );
    }
}

export default TreeOrganization