import * as dayjs from 'dayjs';
import { IEndereco } from 'app/entities/endereco/endereco.model';
import { IUser } from 'app/entities/user/user.model';
import { IAreaAtuacao } from 'app/entities/area-atuacao/area-atuacao.model';

export interface IEmpresa {
  id?: number;
  nome?: string;
  cnpj?: string | null;
  porte?: string | null;
  criado?: dayjs.Dayjs;
  endereco?: IEndereco | null;
  user?: IUser | null;
  area?: IAreaAtuacao | null;
}

export class Empresa implements IEmpresa {
  constructor(
    public id?: number,
    public nome?: string,
    public cnpj?: string | null,
    public porte?: string | null,
    public criado?: dayjs.Dayjs,
    public endereco?: IEndereco | null,
    public user?: IUser | null,
    public area?: IAreaAtuacao | null
  ) {}
}

export function getEmpresaIdentifier(empresa: IEmpresa): number | undefined {
  return empresa.id;
}
