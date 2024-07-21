if  exists(select 1 from sysobjects where id = object_id('px_goods_price') and xtype = 'U')
  drop table dbo.px_goods_price;
Go

create table dbo.px_goods_price
(
  id				bigint  not null,
  createTime		datetime default getdate() not null,			--创建日期
  lastUpdateTime	datetime default getdate() not null,            --最后更新日期
  goodsid			int,											--erp商品编码
  customNo			varchar(32),									--批销系统客户编码
  price				dec(10,3),									    --批销商品单价
  inventory			varchar(32),									--批销库存
  goodsCode			varchar(32),									--批销商品编码
  barCode			varchar(32),									--批销商品条码
  matnr				varchar(32),									--批销大张商品名称
  isDirect			varchar(32),									--批销是否直流
  primary key(id)
);


go


create index i1_px_goods_price on px_goods_price(goodsid);
create unique index i2_px_goods_price on px_goods_price(barCode);
create index i3_px_goods_price on px_goods_price(goodscode);
Go

if  exists(select 1 from sysobjects where id = object_id('px_order_items') and xtype = 'U')
  drop table dbo.px_order_items;
Go


if  exists(select 1 from sysobjects where id = object_id('px_order') and xtype = 'U')
  drop table dbo.px_order;
Go

create table dbo.px_order
(
  id				bigint  not null,
  createTime		datetime default getdate() not null,			--创建日期
  lastUpdateTime	datetime default getdate() not null,            --最后更新日期
  sheetid			varchar(32),									--erp相关单据号
  sheettype			integer,										--erp相关单据类型
  orderNo			varchar(32),									--订单号
  shopNo			varchar(32),									--门店编码
  orderType			varchar(32),								--订单类型
  orderAddress		varchar(255),							--收货地址
  contactPerson		varchar(64),							--收货人
  contactPhone		varchar(32),							--收货联系电话
  orderDate				DateTime,									--订单日期
  deliveryDate		DateTime,									--到店日期：计划发货日期，如果为空则取订单日期
  orderTitle			Varchar(255),							--订单备注
  deliveryStatus  [varchar](4),							--订单状态  0未交货 1部分交货 2全部交货 -1待推送 -2 验收后待推送
  ReceiptSheetId    varchar(32),									--验收单号
  primary key(id)
);


go

create index i1_px_order on px_order(orderno);
create index i2_px_order on px_order(sheetid,sheettype);

Go

CREATE TABLE [dbo].[px_Order_Items](
	[Id] 										[INT] PRIMARY KEY,
	createTime		datetime default getdate() not null,				--创建日期
    lastUpdateTime	datetime default getdate() not null,				--最后更新日期
	[orderNo]								[varchar](32) not null,		--批销订单号
	[GoodsCode] 						[NVARCHAR](32) NULL,			--商品编码
	[BarCode] 							[NVARCHAR](32) NULL,			--商品条码
	[GoodsId]								[INT],						--商品编码(ERP)
	[Num] 									[dec](10,3) NULL,			--数量
	[Unit]									[NVARCHAR](32) NULL,		--单位
	[deliveryNo]						[varchar](32) null,				--交货单号
	[deliveryNum]						[varchar](32) null,				--交货数量
	[price]									[dec](10,3) null,			--价格
	[deliveryStatus]				[varchar](4) null,					--交货状态 0未交货 1部分交货 2全部交货
	[deliveryDate]					[datetime] null,					--交货日期
	[ReceiptQty]					[dec](10,3) default 0,				--验收数量
	[ReceiptDate]					[datetime]							--验收日期
);

go

create index i1_px_Order_Items on px_Order_Items(orderno);
create index i2_px_Order_Items on px_Order_Items(GoodsCode);
create index i3_px_Order_Items on px_Order_Items(BarCode);
create unique index i4_px_Order_Items on px_Order_Items(orderno,GoodsCode,BarCode);
go

if not exists(select 1 from config () where name = 'PX批销供应商')
  insert into config (systemid,name,value,note)
  values(22,'PX批销供应商','-1','-1 没有PX批销供应商');
  
go
