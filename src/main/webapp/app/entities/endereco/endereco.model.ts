import * as dayjs from 'dayjs';

export interface IEndereco {
  id?: number;
  cep?: string;
  logradouro?: string | null;
  complemento?: string | null;
  numero?: string | null;
  uf?: string | null;
  municipio?: string | null;
  bairro?: string | null;
  latitude?: number | null;
  longitude?: number | null;
  criado?: dayjs.Dayjs;
}

export class Endereco implements IEndereco {
  constructor(
    public id?: number,
    public cep?: string,
    public logradouro?: string | null,
    public complemento?: string | null,
    public numero?: string | null,
    public uf?: string | null,
    public municipio?: string | null,
    public bairro?: string | null,
    public latitude?: number | null,
    public longitude?: number | null,
    public criado?: dayjs.Dayjs
  ) {}
}

export function getEnderecoIdentifier(endereco: IEndereco): number | undefined {
  return endereco.id;
}
