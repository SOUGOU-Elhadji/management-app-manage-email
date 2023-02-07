import { IDepartement } from 'app/entities/departement/departement.model';

export interface IPost {
  id: number;
  name_post?: string | null;
  departement?: Pick<IDepartement, 'id' | 'name_departement'> | null;
}

export type NewPost = Omit<IPost, 'id'> & { id: null };
