import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity, updateEntity, createEntity, reset } from './conteneur.reducer';
import { IConteneur } from 'app/shared/model/conteneur.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ConteneurUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const conteneurEntity = useAppSelector(state => state.conteneur.entity);
  const loading = useAppSelector(state => state.conteneur.loading);
  const updating = useAppSelector(state => state.conteneur.updating);
  const updateSuccess = useAppSelector(state => state.conteneur.updateSuccess);
  const handleClose = () => {
    props.history.push('/conteneur');
  };

  useEffect(() => {
    if (!isNew) {
      dispatch(getEntity(props.match.params.id));
    }
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    values.dateEntree = convertDateTimeToServer(values.dateEntree);
    values.dateSortie = convertDateTimeToServer(values.dateSortie);

    const entity = {
      ...conteneurEntity,
      ...values,
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {
          dateEntree: displayDefaultDateTime(),
          dateSortie: displayDefaultDateTime(),
        }
      : {
          ...conteneurEntity,
          dateEntree: convertDateTimeFromServer(conteneurEntity.dateEntree),
          dateSortie: convertDateTimeFromServer(conteneurEntity.dateSortie),
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.conteneur.home.createOrEditLabel" data-cy="ConteneurCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.conteneur.home.createOrEditLabel">Create or edit a Conteneur</Translate>
          </h2>
        </Col>
      </Row>
      <Row className="justify-content-center">
        <Col md="8">
          {loading ? (
            <p>Loading...</p>
          ) : (
            <ValidatedForm defaultValues={defaultValues()} onSubmit={saveEntity}>
              {!isNew ? (
                <ValidatedField
                  name="id"
                  required
                  readOnly
                  id="conteneur-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.conteneur.statut')}
                id="conteneur-statut"
                name="statut"
                data-cy="statut"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.conteneur.dateEntree')}
                id="conteneur-dateEntree"
                name="dateEntree"
                data-cy="dateEntree"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.conteneur.dateSortie')}
                id="conteneur-dateSortie"
                name="dateSortie"
                data-cy="dateSortie"
                type="datetime-local"
                placeholder="YYYY-MM-DD HH:mm"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.conteneur.zone')}
                id="conteneur-zone"
                name="zone"
                data-cy="zone"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.conteneur.ligne')}
                id="conteneur-ligne"
                name="ligne"
                data-cy="ligne"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.conteneur.colonne')}
                id="conteneur-colonne"
                name="colonne"
                data-cy="colonne"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.conteneur.commentaire')}
                id="conteneur-commentaire"
                name="commentaire"
                data-cy="commentaire"
                type="text"
              />
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/conteneur" replace color="info">
                <FontAwesomeIcon icon="arrow-left" />
                &nbsp;
                <span className="d-none d-md-inline">
                  <Translate contentKey="entity.action.back">Back</Translate>
                </span>
              </Button>
              &nbsp;
              <Button color="primary" id="save-entity" data-cy="entityCreateSaveButton" type="submit" disabled={updating}>
                <FontAwesomeIcon icon="save" />
                &nbsp;
                <Translate contentKey="entity.action.save">Save</Translate>
              </Button>
            </ValidatedForm>
          )}
        </Col>
      </Row>
    </div>
  );
};

export default ConteneurUpdate;
