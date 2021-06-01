import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'empresa',
        data: { pageTitle: 'portalVagasEmpregoApp.empresa.home.title' },
        loadChildren: () => import('./empresa/empresa.module').then(m => m.EmpresaModule),
      },
      {
        path: 'vaga',
        data: { pageTitle: 'portalVagasEmpregoApp.vaga.home.title' },
        loadChildren: () => import('./vaga/vaga.module').then(m => m.VagaModule),
      },
      {
        path: 'pessoa',
        data: { pageTitle: 'portalVagasEmpregoApp.pessoa.home.title' },
        loadChildren: () => import('./pessoa/pessoa.module').then(m => m.PessoaModule),
      },
      {
        path: 'funcao-pessoa',
        data: { pageTitle: 'portalVagasEmpregoApp.funcaoPessoa.home.title' },
        loadChildren: () => import('./funcao-pessoa/funcao-pessoa.module').then(m => m.FuncaoPessoaModule),
      },
      {
        path: 'endereco',
        data: { pageTitle: 'portalVagasEmpregoApp.endereco.home.title' },
        loadChildren: () => import('./endereco/endereco.module').then(m => m.EnderecoModule),
      },
      {
        path: 'area-atuacao',
        data: { pageTitle: 'portalVagasEmpregoApp.areaAtuacao.home.title' },
        loadChildren: () => import('./area-atuacao/area-atuacao.module').then(m => m.AreaAtuacaoModule),
      },
      {
        path: 'perfil-profissional',
        data: { pageTitle: 'portalVagasEmpregoApp.perfilProfissional.home.title' },
        loadChildren: () => import('./perfil-profissional/perfil-profissional.module').then(m => m.PerfilProfissionalModule),
      },
      {
        path: 'experiencia-profissional',
        data: { pageTitle: 'portalVagasEmpregoApp.experienciaProfissional.home.title' },
        loadChildren: () => import('./experiencia-profissional/experiencia-profissional.module').then(m => m.ExperienciaProfissionalModule),
      },
      {
        path: 'formacao',
        data: { pageTitle: 'portalVagasEmpregoApp.formacao.home.title' },
        loadChildren: () => import('./formacao/formacao.module').then(m => m.FormacaoModule),
      },
      {
        path: 'curso',
        data: { pageTitle: 'portalVagasEmpregoApp.curso.home.title' },
        loadChildren: () => import('./curso/curso.module').then(m => m.CursoModule),
      },
      {
        path: 'idioma',
        data: { pageTitle: 'portalVagasEmpregoApp.idioma.home.title' },
        loadChildren: () => import('./idioma/idioma.module').then(m => m.IdiomaModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
