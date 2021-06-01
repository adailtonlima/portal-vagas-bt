import * as dayjs from 'dayjs';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { Funcao } from 'app/entities/enumerations/funcao.model';

export interface IFuncaoPessoa {
  id?: number;
  funcao?: Funcao | null;
  criado?: dayjs.Dayjs;
  ativo?: boolean | null;
  pessoa?: IPessoa | null;
}

export class FuncaoPessoa implements IFuncaoPessoa {
  constructor(
    public id?: number,
    public funcao?: Funcao | null,
    public criado?: dayjs.Dayjs,
    public ativo?: boolean | null,
    public pessoa?: IPessoa | null
  ) {
    this.ativo = this.ativo ?? false;
  }
}

export function getFuncaoPessoaIdentifier(funcaoPessoa: IFuncaoPessoa): number | undefined {
  return funcaoPessoa.id;
}
