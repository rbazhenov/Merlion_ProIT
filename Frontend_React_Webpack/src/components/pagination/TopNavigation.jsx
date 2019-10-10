import React from 'react';
import {Link, BrowserRouter}  from 'react-router-dom';
export default class Nav extends React.Component{
    render(){
        return <div>
                    <ul className="nav nav-tabs">
                        <li className="nav-item">
                             <Link className="nav-link" to="/organizations">All organizations</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/workers">All workers</Link>
                        </li>
                        <li className="nav-item">
                             <Link className="nav-link" to="/organizations/tree">Organization's Tree</Link>
                        </li>
                        <li className="nav-item">
                            <Link className="nav-link" to="/workers/tree">Worker's Tree</Link>
                        </li>
                    </ul>
              </div>;
    }
}