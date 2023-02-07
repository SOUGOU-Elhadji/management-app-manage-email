import { IDepartement, NewDepartement } from './departement.model';

export const sampleWithRequiredData: IDepartement = {
  id: 86141,
};

export const sampleWithPartialData: IDepartement = {
  id: 48984,
  name_departement: 'initiatives',
};

export const sampleWithFullData: IDepartement = {
  id: 66903,
  name_departement: 'Account c Inde',
};

export const sampleWithNewData: NewDepartement = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
