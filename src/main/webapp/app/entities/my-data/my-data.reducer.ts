import axios from 'axios';
import { ICrudGetAction, ICrudGetAllAction, ICrudPutAction, ICrudDeleteAction } from 'react-jhipster';

import { cleanEntity } from 'app/shared/util/entity-utils';
import { REQUEST, SUCCESS, FAILURE } from 'app/shared/reducers/action-type.util';

import { IMyData, defaultValue } from 'app/shared/model/my-data.model';

export const ACTION_TYPES = {
  FETCH_MYDATA_LIST: 'myData/FETCH_MYDATA_LIST',
  FETCH_MYDATA: 'myData/FETCH_MYDATA',
  CREATE_MYDATA: 'myData/CREATE_MYDATA',
  UPDATE_MYDATA: 'myData/UPDATE_MYDATA',
  DELETE_MYDATA: 'myData/DELETE_MYDATA',
  RESET: 'myData/RESET'
};

const initialState = {
  loading: false,
  errorMessage: null,
  entities: [] as ReadonlyArray<IMyData>,
  entity: defaultValue,
  updating: false,
  updateSuccess: false
};

export type MyDataState = Readonly<typeof initialState>;

// Reducer

export default (state: MyDataState = initialState, action): MyDataState => {
  switch (action.type) {
    case REQUEST(ACTION_TYPES.FETCH_MYDATA_LIST):
    case REQUEST(ACTION_TYPES.FETCH_MYDATA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        loading: true
      };
    case REQUEST(ACTION_TYPES.CREATE_MYDATA):
    case REQUEST(ACTION_TYPES.UPDATE_MYDATA):
    case REQUEST(ACTION_TYPES.DELETE_MYDATA):
      return {
        ...state,
        errorMessage: null,
        updateSuccess: false,
        updating: true
      };
    case FAILURE(ACTION_TYPES.FETCH_MYDATA_LIST):
    case FAILURE(ACTION_TYPES.FETCH_MYDATA):
    case FAILURE(ACTION_TYPES.CREATE_MYDATA):
    case FAILURE(ACTION_TYPES.UPDATE_MYDATA):
    case FAILURE(ACTION_TYPES.DELETE_MYDATA):
      return {
        ...state,
        loading: false,
        updating: false,
        updateSuccess: false,
        errorMessage: action.payload
      };
    case SUCCESS(ACTION_TYPES.FETCH_MYDATA_LIST):
      return {
        ...state,
        loading: false,
        entities: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.FETCH_MYDATA):
      return {
        ...state,
        loading: false,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.CREATE_MYDATA):
    case SUCCESS(ACTION_TYPES.UPDATE_MYDATA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: action.payload.data
      };
    case SUCCESS(ACTION_TYPES.DELETE_MYDATA):
      return {
        ...state,
        updating: false,
        updateSuccess: true,
        entity: {}
      };
    case ACTION_TYPES.RESET:
      return {
        ...initialState
      };
    default:
      return state;
  }
};

const apiUrl = 'api/my-data';

// Actions

export const getEntities: ICrudGetAllAction<IMyData> = (page, size, sort) => ({
  type: ACTION_TYPES.FETCH_MYDATA_LIST,
  payload: axios.get<IMyData>(`${apiUrl}?cacheBuster=${new Date().getTime()}`)
});

export const getEntity: ICrudGetAction<IMyData> = id => {
  const requestUrl = `${apiUrl}/${id}`;
  return {
    type: ACTION_TYPES.FETCH_MYDATA,
    payload: axios.get<IMyData>(requestUrl)
  };
};

export const createEntity: ICrudPutAction<IMyData> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.CREATE_MYDATA,
    payload: axios.post(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const updateEntity: ICrudPutAction<IMyData> = entity => async dispatch => {
  const result = await dispatch({
    type: ACTION_TYPES.UPDATE_MYDATA,
    payload: axios.put(apiUrl, cleanEntity(entity))
  });
  dispatch(getEntities());
  return result;
};

export const deleteEntity: ICrudDeleteAction<IMyData> = id => async dispatch => {
  const requestUrl = `${apiUrl}/${id}`;
  const result = await dispatch({
    type: ACTION_TYPES.DELETE_MYDATA,
    payload: axios.delete(requestUrl)
  });
  dispatch(getEntities());
  return result;
};

export const reset = () => ({
  type: ACTION_TYPES.RESET
});
