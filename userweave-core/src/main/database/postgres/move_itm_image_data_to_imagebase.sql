insert into imagebase (id,clientfilename,imagedata,mimetype) select id, clientfilename,imagedata,mimetype from itm_image;

alter table itm_image drop column mimetype;

alter table itm_image drop column imagedata;

alter table itm_image drop column clientfilename;

commit;