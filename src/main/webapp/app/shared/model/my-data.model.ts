import { Moment } from 'moment';

export interface IMyData {
  id?: number;
  firstDate?: Moment;
  lasteDate?: Moment;
}

export const defaultValue: Readonly<IMyData> = {};
