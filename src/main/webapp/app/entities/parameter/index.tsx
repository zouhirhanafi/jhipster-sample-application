import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Parameter from './parameter';
import ParameterDetail from './parameter-detail';
import ParameterUpdate from './parameter-update';
import ParameterDeleteDialog from './parameter-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ParameterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ParameterUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ParameterDetail} />
      <ErrorBoundaryRoute path={match.url} component={Parameter} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ParameterDeleteDialog} />
  </>
);

export default Routes;
