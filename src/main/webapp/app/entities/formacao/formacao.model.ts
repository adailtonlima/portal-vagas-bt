import * as dayjs from 'dayjs';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';

export interface IFormacao {
  id?: number;
  instituicao?: string | null;
  tipo?: string | null;
  inicio?: dayjs.Dayjs | null;
  conclusao?: dayjs.Dayjs | null;
  criado?: dayjs.Dayjs;
  pessoa?: IPessoa | null;
}

export class Formacao implements IFormacao {
  constructor(
    public id?: number,
    public instituicao?: string | null,
    public tipo?: string | null,
    public inicio?: dayjs.Dayjs | null,
    public conclusao?: dayjs.Dayjs | null,
    public criado?: dayjs.Dayjs,
    public pessoa?: IPessoa | null
  ) {}
}

export function getFormacaoIdentifier(formacao: IFormacao): number | undefined {
  return formacao.id;
}
