import * as dayjs from 'dayjs';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { IUser } from 'app/entities/user/user.model';

export interface IPessoa {
  id?: number;
  nome?: string;
  email?: string | null;
  dataNascimento?: dayjs.Dayjs | null;
  cpf?: string | null;
  telefone?: string | null;
  nacionalidade?: string | null;
  naturalidade?: string | null;
  sexo?: string | null;
  estadoCivil?: string | null;
  pcd?: boolean | null;
  pcdCID?: string | null;
  cnh?: string | null;
  fotoUrl?: string | null;
  criado?: dayjs.Dayjs;
  endereco?: IEndereco | null;
  user?: IUser | null;
}

export class Pessoa implements IPessoa {
  constructor(
    public id?: number,
    public nome?: string,
    public email?: string | null,
    public dataNascimento?: dayjs.Dayjs | null,
    public cpf?: string | null,
    public telefone?: string | null,
    public nacionalidade?: string | null,
    public naturalidade?: string | null,
    public sexo?: string | null,
    public estadoCivil?: string | null,
    public pcd?: boolean | null,
    public pcdCID?: string | null,
    public cnh?: string | null,
    public fotoUrl?: string | null,
    public criado?: dayjs.Dayjs,
    public endereco?: IEndereco | null,
    public user?: IUser | null
  ) {
    this.pcd = this.pcd ?? false;
  }
}

export function getPessoaIdentifier(pessoa: IPessoa): number | undefined {
  return pessoa.id;
}
