import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { LibreriaSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective } from './';

@NgModule({
  imports: [LibreriaSharedCommonModule],
  declarations: [JhiLoginModalComponent, HasAnyAuthorityDirective],
  entryComponents: [JhiLoginModalComponent],
  exports: [LibreriaSharedCommonModule, JhiLoginModalComponent, HasAnyAuthorityDirective],
  schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class LibreriaSharedModule {
  static forRoot() {
    return {
      ngModule: LibreriaSharedModule
    };
  }
}
