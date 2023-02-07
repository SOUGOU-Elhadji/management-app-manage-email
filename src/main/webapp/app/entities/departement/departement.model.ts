export interface IDepartement {
  id: number;
  name_departement?: string | null;
}

export type NewDepartement = Omit<IDepartement, 'id'> & { id: null };
