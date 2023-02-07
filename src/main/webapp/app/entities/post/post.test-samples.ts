import { IPost, NewPost } from './post.model';

export const sampleWithRequiredData: IPost = {
  id: 35989,
};

export const sampleWithPartialData: IPost = {
  id: 8512,
};

export const sampleWithFullData: IPost = {
  id: 37108,
  name_post: 'Personal',
};

export const sampleWithNewData: NewPost = {
  id: null,
};

Object.freeze(sampleWithNewData);
Object.freeze(sampleWithRequiredData);
Object.freeze(sampleWithPartialData);
Object.freeze(sampleWithFullData);
