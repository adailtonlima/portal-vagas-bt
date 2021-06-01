import * as dayjs from 'dayjs';
import { IAreaAtuacao } from 'app/entities/area-atuacao/area-atuacao.model';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';

export interface IPerfilProfissional {
  id?: number;
  estagio?: boolean | null;
  procurandoEmprego?: boolean | null;
  objetivosPessoais?: string | null;
  pretensaoSalarial?: number | null;
  criado?: dayjs.Dayjs;
  area?: IAreaAtuacao | null;
  pessoa?: IPessoa | null;
}

export class PerfilProfissional implements IPerfilProfissional {
  constructor(
    public id?: number,
    public estagio?: boolean | null,
    public procurandoEmprego?: boolean | null,
    public objetivosPessoais?: string | null,
    public pretensaoSalarial?: number | null,
    public criado?: dayjs.Dayjs,
    public area?: IAreaAtuacao | null,
    public pessoa?: IPessoa | null
  ) {
    this.estagio = this.estagio ?? false;
    this.procurandoEmprego = this.procurandoEmprego ?? false;
  }
}

export function getPerfilProfissionalIdentifier(perfilProfissional: IPerfilProfissional): number | undefined {
  return perfilProfissional.id;
}
