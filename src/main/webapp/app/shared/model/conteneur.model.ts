import dayjs from 'dayjs';

export interface IConteneur {
  id?: number;
  statut?: number | null;
  dateEntree?: string | null;
  dateSortie?: string | null;
  zone?: number | null;
  ligne?: number | null;
  colonne?: number | null;
  commentaire?: string | null;
}

export const defaultValue: Readonly<IConteneur> = {};
