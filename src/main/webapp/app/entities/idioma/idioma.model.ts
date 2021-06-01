import * as dayjs from 'dayjs';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { NivelIdioma } from 'app/entities/enumerations/nivel-idioma.model';

export interface IIdioma {
  id?: number;
  nome?: string | null;
  nivel?: NivelIdioma | null;
  criado?: dayjs.Dayjs;
  pessoa?: IPessoa | null;
}

export class Idioma implements IIdioma {
  constructor(
    public id?: number,
    public nome?: string | null,
    public nivel?: NivelIdioma | null,
    public criado?: dayjs.Dayjs,
    public pessoa?: IPessoa | null
  ) {}
}

export function getIdiomaIdentifier(idioma: IIdioma): number | undefined {
  return idioma.id;
}
