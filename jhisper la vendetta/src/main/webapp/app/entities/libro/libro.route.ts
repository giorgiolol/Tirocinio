import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Libro } from 'app/shared/model/libro.model';
import { LibroService } from './libro.service';
import { LibroComponent } from './libro.component';
import { LibroDetailComponent } from './libro-detail.component';
import { LibroUpdateComponent } from './libro-update.component';
import { LibroDeletePopupComponent } from './libro-delete-dialog.component';
import { ILibro } from 'app/shared/model/libro.model';

@Injectable({ providedIn: 'root' })
export class LibroResolve implements Resolve<ILibro> {
  constructor(private service: LibroService) {}

  resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<ILibro> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        filter((response: HttpResponse<Libro>) => response.ok),
        map((libro: HttpResponse<Libro>) => libro.body)
      );
    }
    return of(new Libro());
  }
}

export const libroRoute: Routes = [
  {
    path: '',
    component: LibroComponent,
    resolve: {
      pagingParams: JhiResolvePagingParams
    },
    data: {
      authorities: ['ROLE_USER'],
      defaultSort: 'id,asc',
      pageTitle: 'libreriaApp.libro.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/view',
    component: LibroDetailComponent,
    resolve: {
      libro: LibroResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'libreriaApp.libro.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: 'new',
    component: LibroUpdateComponent,
    resolve: {
      libro: LibroResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'libreriaApp.libro.home.title'
    },
    canActivate: [UserRouteAccessService]
  },
  {
    path: ':id/edit',
    component: LibroUpdateComponent,
    resolve: {
      libro: LibroResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'libreriaApp.libro.home.title'
    },
    canActivate: [UserRouteAccessService]
  }
];

export const libroPopupRoute: Routes = [
  {
    path: ':id/delete',
    component: LibroDeletePopupComponent,
    resolve: {
      libro: LibroResolve
    },
    data: {
      authorities: ['ROLE_USER'],
      pageTitle: 'libreriaApp.libro.home.title'
    },
    canActivate: [UserRouteAccessService],
    outlet: 'popup'
  }
];
