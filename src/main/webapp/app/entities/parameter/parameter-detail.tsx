import React, { useEffect } from 'react';
import { Link, RouteComponentProps } from 'react-router-dom';
import { Button, Row, Col } from 'reactstrap';
import { Translate } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';

import { getEntity } from './parameter.reducer';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { useAppDispatch, useAppSelector } from 'app/config/store';

export const ParameterDetail = (props: RouteComponentProps<{ id: string }>) => {
  const dispatch = useAppDispatch();

  useEffect(() => {
    dispatch(getEntity(props.match.params.id));
  }, []);

  const parameterEntity = useAppSelector(state => state.parameter.entity);
  return (
    <Row>
      <Col md="8">
        <h2 data-cy="parameterDetailsHeading">
          <Translate contentKey="jhipsterSampleApplicationApp.parameter.detail.title">Parameter</Translate>
        </h2>
        <dl className="jh-entity-details">
          <dt>
            <span id="id">
              <Translate contentKey="global.field.id">ID</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.id}</dd>
          <dt>
            <span id="label">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.label">Label</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.label}</dd>
          <dt>
            <span id="activated">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.activated">Activated</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.activated ? 'true' : 'false'}</dd>
          <dt>
            <span id="lib2">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.lib2">Lib 2</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.lib2}</dd>
          <dt>
            <span id="lib3">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.lib3">Lib 3</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.lib3}</dd>
          <dt>
            <span id="refExterne">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.refExterne">Ref Externe</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.refExterne}</dd>
          <dt>
            <span id="val1">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.val1">Val 1</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.val1}</dd>
          <dt>
            <span id="val2">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.val2">Val 2</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.val2}</dd>
          <dt>
            <span id="val3">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.val3">Val 3</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.val3}</dd>
          <dt>
            <span id="ordre">
              <Translate contentKey="jhipsterSampleApplicationApp.parameter.ordre">Ordre</Translate>
            </span>
          </dt>
          <dd>{parameterEntity.ordre}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.parameter.type">Type</Translate>
          </dt>
          <dd>{parameterEntity.type ? parameterEntity.type.id : ''}</dd>
          <dt>
            <Translate contentKey="jhipsterSampleApplicationApp.parameter.paraent">Paraent</Translate>
          </dt>
          <dd>{parameterEntity.paraent ? parameterEntity.paraent.id : ''}</dd>
        </dl>
        <Button tag={Link} to="/parameter" replace color="info" data-cy="entityDetailsBackButton">
          <FontAwesomeIcon icon="arrow-left" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.back">Back</Translate>
          </span>
        </Button>
        &nbsp;
        <Button tag={Link} to={`/parameter/${parameterEntity.id}/edit`} replace color="primary">
          <FontAwesomeIcon icon="pencil-alt" />{' '}
          <span className="d-none d-md-inline">
            <Translate contentKey="entity.action.edit">Edit</Translate>
          </span>
        </Button>
      </Col>
    </Row>
  );
};

export default ParameterDetail;
