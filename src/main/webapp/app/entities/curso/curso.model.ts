import * as dayjs from 'dayjs';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';

export interface ICurso {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  periodo?: dayjs.Dayjs | null;
  criado?: dayjs.Dayjs;
  pessoa?: IPessoa | null;
}

export class Curso implements ICurso {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public periodo?: dayjs.Dayjs | null,
    public criado?: dayjs.Dayjs,
    public pessoa?: IPessoa | null
  ) {}
}

export function getCursoIdentifier(curso: ICurso): number | undefined {
  return curso.id;
}
