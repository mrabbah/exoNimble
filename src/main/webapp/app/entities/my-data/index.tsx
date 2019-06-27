import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import MyData from './my-data';
import MyDataDetail from './my-data-detail';
import MyDataUpdate from './my-data-update';
import MyDataDeleteDialog from './my-data-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={MyDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={MyDataUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={MyDataDetail} />
      <ErrorBoundaryRoute path={match.url} component={MyData} />
    </Switch>
    <ErrorBoundaryRoute path={`${match.url}/:id/delete`} component={MyDataDeleteDialog} />
  </>
);

export default Routes;
