import * as dayjs from 'dayjs';
import { IPessoa } from 'app/entities/pessoa/pessoa.model';
import { IEmpresa } from 'app/entities/empresa/empresa.model';

export interface IVaga {
  id?: number;
  descricao?: string;
  titulo?: string;
  estagio?: boolean | null;
  salario?: number | null;
  beneficios?: number | null;
  jornadaSemanal?: number | null;
  bannerUrl?: string | null;
  fonte?: string | null;
  linkRecrutamento?: string | null;
  ativo?: boolean | null;
  preenchida?: boolean | null;
  criado?: dayjs.Dayjs;
  prazoAnuncio?: dayjs.Dayjs | null;
  cadastrou?: IPessoa | null;
  empresa?: IEmpresa | null;
}

export class Vaga implements IVaga {
  constructor(
    public id?: number,
    public descricao?: string,
    public titulo?: string,
    public estagio?: boolean | null,
    public salario?: number | null,
    public beneficios?: number | null,
    public jornadaSemanal?: number | null,
    public bannerUrl?: string | null,
    public fonte?: string | null,
    public linkRecrutamento?: string | null,
    public ativo?: boolean | null,
    public preenchida?: boolean | null,
    public criado?: dayjs.Dayjs,
    public prazoAnuncio?: dayjs.Dayjs | null,
    public cadastrou?: IPessoa | null,
    public empresa?: IEmpresa | null
  ) {
    this.estagio = this.estagio ?? false;
    this.ativo = this.ativo ?? false;
    this.preenchida = this.preenchida ?? false;
  }
}

export function getVagaIdentifier(vaga: IVaga): number | undefined {
  return vaga.id;
}
