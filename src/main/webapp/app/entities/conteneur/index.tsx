import React from 'react';
import { Switch } from 'react-router-dom';

import ErrorBoundaryRoute from 'app/shared/error/error-boundary-route';

import Conteneur from './conteneur';
import ConteneurDetail from './conteneur-detail';
import ConteneurUpdate from './conteneur-update';
import ConteneurDeleteDialog from './conteneur-delete-dialog';

const Routes = ({ match }) => (
  <>
    <Switch>
      <ErrorBoundaryRoute exact path={`${match.url}/new`} component={ConteneurUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id/edit`} component={ConteneurUpdate} />
      <ErrorBoundaryRoute exact path={`${match.url}/:id`} component={ConteneurDetail} />
      <ErrorBoundaryRoute path={match.url} component={Conteneur} />
    </Switch>
    <ErrorBoundaryRoute exact path={`${match.url}/:id/delete`} component={ConteneurDeleteDialog} />
  </>
);

export default Routes;
