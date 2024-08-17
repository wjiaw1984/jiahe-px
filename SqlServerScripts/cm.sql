if exists(select 1 from dbo.sysobjects where id = object_id('dbo.px_order_log0') and xtype = 'U')
  drop table dbo.px_order_log0
Go
Create table dbo.px_order_log0
(
	SerialID	 	int identity 	primary key,					--序号主键
	CreateDate		datetime		not null default getdate(),		--创建时间
	refSheetType	int				not null,						--单据类型
	refSheetId		varchar(32)     not null,						--相关单号
	[status]		varchar(2)		not null default '1',			--处理状态，1，新建待处理.2.正在处理写批销订单表.99.发生异常，异常信息查看msg字段;90待备份处理;100外理完结
	lastUpdateDate	datetime,										--最后处理时间
	Msg				varchar(2000)									--处理信息

)
Go


if exists(select 1 from dbo.sysobjects where id = object_id('dbo.px_order_log') and xtype = 'U')
  drop table dbo.px_order_log
Go
Create table dbo.px_order_log
(
	SerialID	 	int			 	primary key,					--序号主键
	CreateDate		datetime		not null default getdate(),		--创建时间
	refSheetType	int				not null,						--单据类型
	refSheetId		varchar(32)     not null,						--相关单号
	[status]		varchar(2)		not null default '1',			--处理状态，1，新建待处理.2.正在处理写批销订单表.99.发生异常，异常信息查看msg字段;90待备份处理;  100外理完结
	lastUpdateDate	datetime,										--最后处理时间
	Msg				varchar(2000)									--处理信息
)
Go

if exists (select * from dbo.sysobjects 
   where id = object_id(N'[dbo].[Tr_InsertPurchase]') and OBJECTPROPERTY(id, N'IsTrigger') = 1)
  drop trigger [dbo].[Tr_InsertPurchase]
GO

create TRIGGER dbo.Tr_InsertPurchase ON Purchase 
FOR Insert AS
  declare @PXVenderId	int;

  select @pxvenderid = value from config where name = 'PX批销供应商'
  if @pxvenderid is null set @pxvenderid = -1;

   
  select 2001,SheetID from Inserted where VenderID = @pxvenderid
  and exists(select * from shop where id = Inserted.shopid and shoptype > 10 and shoptype < 90 and shoptype != 21)
GO

if exists (select * from dbo.sysobjects 
   where id = object_id(N'[dbo].[Tr_InsertReceipt]') and OBJECTPROPERTY(id, N'IsTrigger') = 1)
  drop trigger [dbo].[Tr_InsertReceipt]
GO

create TRIGGER dbo.Tr_InsertReceipt ON Receipt 
FOR Insert AS
  declare @PXVenderId	int;

  select @pxvenderid = value from config where name = 'PX批销供应商'
  if @pxvenderid is null set @pxvenderid = -1;

  
    insert into px_order_log0 (refSheetType,refSheetId)
    select 2002,SheetID from Inserted where flag = 100 and VenderID = @pxvenderid
      and exists(select 1 from shop where id = shopid and shoptype > 10 and shoptype < 90 and shoptype != 21)
GO


--视图,按实际情况修改
if exists(select 1 from sysobjects where id = object_id('v_px_order') and xtype = 'V')
  drop view dbo.v_px_order
Go

Create View dbo.v_px_order as
  select * from test.dbo.px_order;
Go

if exists(select 1 from sysobjects where id = object_id('v_px_order_items') and xtype = 'V')
  drop view dbo.v_px_order_items
Go

Create View dbo.v_px_order_items as
  select * from test.dbo.px_Order_Items
Go



