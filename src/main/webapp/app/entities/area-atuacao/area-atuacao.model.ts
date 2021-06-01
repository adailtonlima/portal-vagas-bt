import * as dayjs from 'dayjs';

export interface IAreaAtuacao {
  id?: number;
  nome?: string | null;
  descricao?: string | null;
  iconeUrl?: string | null;
  ativo?: boolean | null;
  criado?: dayjs.Dayjs;
}

export class AreaAtuacao implements IAreaAtuacao {
  constructor(
    public id?: number,
    public nome?: string | null,
    public descricao?: string | null,
    public iconeUrl?: string | null,
    public ativo?: boolean | null,
    public criado?: dayjs.Dayjs
  ) {
    this.ativo = this.ativo ?? false;
  }
}

export function getAreaAtuacaoIdentifier(areaAtuacao: IAreaAtuacao): number | undefined {
  return areaAtuacao.id;
}
