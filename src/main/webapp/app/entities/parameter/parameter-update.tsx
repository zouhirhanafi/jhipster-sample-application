import React, { useState, useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col, FormText } from 'reactstrap';
import { isNumber, Translate, translate, ValidatedField, ValidatedForm } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntities as getParameters } from 'app/entities/parameter/parameter.reducer';
import { getEntity, updateEntity, createEntity, reset } from './parameter.reducer';
import { IParameter } from 'app/shared/model/parameter.model';
import { convertDateTimeFromServer, convertDateTimeToServer, displayDefaultDateTime } from 'app/shared/util/date-utils';
import { mapIdList } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ParameterUpdate = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  const [isNew] = useState(!props.match.params || !props.match.params.id);

  const parameters = useAppSelector(state => state.parameter.entities);
  const parameterEntity = useAppSelector(state => state.parameter.entity);
  const loading = useAppSelector(state => state.parameter.loading);
  const updating = useAppSelector(state => state.parameter.updating);
  const updateSuccess = useAppSelector(state => state.parameter.updateSuccess);
  const handleClose = () => {
    props.history.push('/parameter' + props.location.search);
  };

  useEffect(() => {
    if (isNew) {
      dispatch(reset());
    } else {
      dispatch(getEntity(props.match.params.id));
    }

    dispatch(getParameters({}));
  }, []);

  useEffect(() => {
    if (updateSuccess) {
      handleClose();
    }
  }, [updateSuccess]);

  const saveEntity = values => {
    const entity = {
      ...parameterEntity,
      ...values,
      type: parameters.find(it => it.id.toString() === values.type.toString()),
      paraent: parameters.find(it => it.id.toString() === values.paraent.toString()),
    };

    if (isNew) {
      dispatch(createEntity(entity));
    } else {
      dispatch(updateEntity(entity));
    }
  };

  const defaultValues = () =>
    isNew
      ? {}
      : {
          ...parameterEntity,
          type: parameterEntity?.type?.id,
          paraent: parameterEntity?.paraent?.id,
        };

  return (
    <div>
      <Row className="justify-content-center">
        <Col md="8">
          <h2 id="jhipsterSampleApplicationApp.parameter.home.createOrEditLabel" data-cy="ParameterCreateUpdateHeading">
            <Translate contentKey="jhipsterSampleApplicationApp.parameter.home.createOrEditLabel">Create or edit a Parameter</Translate>
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
                  id="parameter-id"
                  label={translate('global.field.id')}
                  validate={{ required: true }}
                />
              ) : null}
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.label')}
                id="parameter-label"
                name="label"
                data-cy="label"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.activated')}
                id="parameter-activated"
                name="activated"
                data-cy="activated"
                check
                type="checkbox"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.lib2')}
                id="parameter-lib2"
                name="lib2"
                data-cy="lib2"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.lib3')}
                id="parameter-lib3"
                name="lib3"
                data-cy="lib3"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.refExterne')}
                id="parameter-refExterne"
                name="refExterne"
                data-cy="refExterne"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.val1')}
                id="parameter-val1"
                name="val1"
                data-cy="val1"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.val2')}
                id="parameter-val2"
                name="val2"
                data-cy="val2"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.val3')}
                id="parameter-val3"
                name="val3"
                data-cy="val3"
                type="text"
              />
              <ValidatedField
                label={translate('jhipsterSampleApplicationApp.parameter.ordre')}
                id="parameter-ordre"
                name="ordre"
                data-cy="ordre"
                type="text"
              />
              <ValidatedField
                id="parameter-type"
                name="type"
                data-cy="type"
                label={translate('jhipsterSampleApplicationApp.parameter.type')}
                type="select"
              >
                <option value="" key="0" />
                {parameters
                  ? parameters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <ValidatedField
                id="parameter-paraent"
                name="paraent"
                data-cy="paraent"
                label={translate('jhipsterSampleApplicationApp.parameter.paraent')}
                type="select"
              >
                <option value="" key="0" />
                {parameters
                  ? parameters.map(otherEntity => (
                      <option value={otherEntity.id} key={otherEntity.id}>
                        {otherEntity.id}
                      </option>
                    ))
                  : null}
              </ValidatedField>
              <Button tag={Link} id="cancel-save" data-cy="entityCreateCancelButton" to="/parameter" replace color="info">
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

export default ParameterUpdate;
