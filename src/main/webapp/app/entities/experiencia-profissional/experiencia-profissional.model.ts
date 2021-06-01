import * as dayjs from 'dayjs';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';

export interface IExperienciaProfissional {
  id?: number;
  empresa?: string | null;
  cargo?: string | null;
  segmento?: string | null;
  porte?: string | null;
  inicio?: dayjs.Dayjs | null;
  fim?: dayjs.Dayjs | null;
  descricaoAtividade?: string | null;
  salario?: number | null;
  beneficios?: number | null;
  criado?: dayjs.Dayjs;
  pessoa?: IPessoa | null;
}

export class ExperienciaProfissional implements IExperienciaProfissional {
  constructor(
    public id?: number,
    public empresa?: string | null,
    public cargo?: string | null,
    public segmento?: string | null,
    public porte?: string | null,
    public inicio?: dayjs.Dayjs | null,
    public fim?: dayjs.Dayjs | null,
    public descricaoAtividade?: string | null,
    public salario?: number | null,
    public beneficios?: number | null,
    public criado?: dayjs.Dayjs,
    public pessoa?: IPessoa | null
  ) {}
}

export function getExperienciaProfissionalIdentifier(experienciaProfissional: IExperienciaProfissional): number | undefined {
  return experienciaProfissional.id;
}
