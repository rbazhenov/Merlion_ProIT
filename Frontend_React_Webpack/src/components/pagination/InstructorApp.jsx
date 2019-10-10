import React, { Component } from 'react';
import ListOrganizations from '../ListOrganizations.jsx';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom'
import SoloOrganization from '../SoloOrganization.jsx';
import ListWorkersComponent from '../ListWorker.jsx';
import SoloWorker from '../SoloWorker.jsx';
import Nav from './TopNavigation.jsx';
import PageNotFound from './PageNotFound.jsx';
import PageStart from './PageStart.jsx';
import TreeOrganization from '../TreeOrganization.jsx'
import TreeWorker from '../TreeWorker.jsx'

class InstructorApp extends Component {
    render() {
        return (
            <Router>
                <div>
                    <h1>bmth</h1>
                    <Nav/>
                    <Switch>
                        <Route path="/" exact component={PageStart} />

                        <Route exact path="/organizations" component={ListOrganizations} />
                        <Route exact path="/organizations/id/:id" component={SoloOrganization} />
                        <Route exact path="/organizations/search" component={ListOrganizations} />
                        <Route exact path="/organizations/tree" component={TreeOrganization}/>

                        <Route exact path="/workers" component={ListWorkersComponent} />
                        <Route exact path="/workers/id/:id" component={SoloWorker} />
                        <Route exact path="/workers/search" component={ListWorkersComponent} />
                        <Route exact path="/workers/tree" component={TreeWorker}/>

                        <Route component={PageNotFound} />
                    </Switch>

                </div>
            </Router>
        )
    }
}
export default InstructorApp