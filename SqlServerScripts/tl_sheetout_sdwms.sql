USE mySHOPDCStock
GO
SET ANSI_NULLS, QUOTED_IDENTIFIER ON
GO

CREATE PROCEDURE [dbo].[TL_SheetOut_SDWMS](@InterfaceSystem CHAR(8),@SheetID CHAR(32),@SheetType INT,@Out_ShopID VARCHAR(6) OUTPUT,@Out_SheetID VARCHAR(20) OUTPUT)
---------------------------------------------------------------------------------
--TL_SheetOut_SDWMS			标准数据下传接口处理
--说明：
--参数：
--返回：0=成功
--建立：郭振滔 2012-4-9 数据下传执行过程
--说明：同服务器跨库操作
--修改: 2012.4.18 郭振滔 修正配送出库单下传报错 		+1
--修改: 郭振滔 2012.5.10 增加盘点和退货接口
--修改: 郭振滔 2012.5.15 增加生鲜标志
--修改: 郭振滔 2012.5.16 解决盘点通知单下传报错....
--修改：郭振滔 2012.5.18 下传部类资料的时候不写死，根据配置走....
--修改: 郭振滔 2012.5.23 供应商资料接口增加OrderFlag('Y'=约仓标志，不等于'Y'为不约仓)
--修改: 郭振滔 2012.5.25 WMS_Goods的SGroup保存小类......
--修改: 郭振滔 2012.5.25 WMS_SGroup增加小类信息.........
--修改: 郭振滔 2012.5.25 退货和返配的单子要传入ReasonTypeID退货原因编码
--修改: 郭振滔 2012.5.25 配送单下传WMS增加好坏货.....
--修改: 郭振滔 2012.5.25 下传商品基础信息增加保鲜期
--修改: 郭振滔 2012.5.28 商品Sgroup表保存为小类，生鲜标志更新对应更改
--修改: 郭振滔 2012.5.29 修正返配单接口ReasonTypeID没有数据的BUG，返配单也带上好坏货标志....
--修改: 郭振滔 2012.6.1  增加订单过期接口和仓间调拨接口....    +1
--修改: 郭振滔 2012.6.27 订单下发给WMS的时候，增加一列记录订单号，之前的RefSheetID记录的是订货审批单号..
--修改: 郭振滔 2012.7.5  增加批发单接口	+1
--修改: 郭振滔 2012.9.3  退货单批次库存不足的时候修改通知数量..........   +1
--修改: 郭振滔 2012.9.28 重建事务控制.....
--修改: 郭振滔 2012.9.28 增加报损单和行政领用单...
--修改: 郭振滔 2012.10.22 增加传递配送审批单...
--修改: 郭振滔 2012.11.1 遵循标准接口规范调整....
--修改: 齐润   供应商增加约仓标志....(by gzt)
--修改: 郭振滔 2012.11.14 修正基础资料下传报错...
--修改：郭振滔 2012.11.23 解决配送单下传RefSheetID为空的问题
--修改：qirun 2012.11.27 空单标识+1
--修改: 郭振滔 2012.11.28 增加SerialID无数据的处理。
--修改: 郭振滔 2012.11.29 直通配送单的RefSheetID增加前缀，否则无法识别。(区分直通大单和直通小单)
--修改: 郭振滔 2013.1.5   订单取消WMS接口里面，0=完结，1=有效期延长，跟单据正好是相反的。
--修改: 郭振滔 2013.1.31 增加生鲜部类批次进价调整单下传WMS的接口....
--修改: 郭振滔 2013.4.2  批发单会出现planqty为0的情况....
--修改: 郭振滔 2013.5.9  进售价变更时，不同步非冷链物流商品的资料到接口表
--修改：丘丹 2013.11.7 移植至R5.
--修改：丘丹 2015.07.07 增加行政退货的接口。
--修改：丘丹 2016.12.26 增加区域调拨通知单接口[2341],调拨接口[2344]。
--修改：caichengming 2017-09-13 新增批发通知单传WMS接口,通知单接口单据类型sheettype=2450 +2
--修改：caichengming 2018-02-08 调拨入库单SDWMS_PURCHASE.VenderID取值错误 +1
--修改：丘丹 2018.07.13 纠正WMS序号重复问题.
--修改：赖俊云 2023.2.16  e157939 物流接口传递大直通订单有效期问题
----------------------------------------------------------------------------------
AS BEGIN
  DECLARE @Err INT;
  DECLARE @BreakPoint INT;
  DECLARE @ISAM INT;
  DECLARE @Msg VARCHAR(255);
  DECLARE @NewSheetID CHAR(32)
  DECLARE @RefSheetID CHAR(32);
  DECLARE @ShopID CHAR(6);
  DECLARE @OutShopID CHAR(6);
  DECLARE @InShopID CHAR(6);
  DECLARE @DCShopID CHAR(6);
  DECLARE @MaxSerialID INT;
  DECLARE @SerialID INT;
  DECLARE @InOutType CHAR(1);
  DECLARE @DirectFlag INT;
  DECLARE @CustomID VARCHAR(10);
  DECLARE @SendType INT;
  DECLARE @Addr VARCHAR(64);
  DECLARE @VenderID INT;
  DECLARE @Reason CHAR(32);
  DECLARE @Sdate DATETIME;
  DECLARE @purDate DATETIME;
  DECLARE @validDate DATETIME;
  DECLARE @BSDate DATETIME;
  DECLARE @SendDate DATETIME;
  DECLARE @Type INT;
  DECLARE @PDFlag INT;
  DECLARE @Purday INT;
  DECLARE @Logistics INT;
  DECLARE @RefSheetType INT;
  DECLARE @Flag INT;
  DECLARE @DeptValue INT;
  DECLARE @RType INT;
  DECLARE @Notes VARCHAR(256);
  DECLARE @LevelValue INT;
  DECLARE @PurSheetID VARCHAR(20)
  DECLARE @DelFlag INT;
  DECLARE @HeadShopID CHAR(4);
  DECLARE @HeadShopType INT;
  DECLARE @MyShopID CHAR(4);
  DECLARE @MyShopType INT;
  DECLARE @OrderPKNumType INT;
  DECLARE @GoodsID INT;
  DECLARE @PKNum INT; --订货数量
  DECLARE @PKName CHAR(8); --订货单位
  DECLARE @PKSpec CHAR(32); --订货规格
  DECLARE @RetFlag int;
  declare @EMFlag int;
  declare @WMSSheetID varchar(50);
  declare @NoSendWMSManage varchar(1000) --不传WMS部类
  declare @FreshManageDeptID varchar(1000);
  declare @SQL varchar(1000);
  declare @PlanQty dec(12, 4);
  declare @SumQty dec(12, 4);
  declare @Count int;
  declare @ManageDeptID varchar(20);
  declare @FreshWMSManage varchar(1000); --冷链仓库管理部类
  declare @FreshWMSFlag int; --0=常温仓 1=低温仓
  declare @PalletZone varchar(10); --上位机编码
  declare @LevelType int;
  declare @PurFlag int;
  declare @BadFlag int;
  declare @EmptyFlag int;
  declare @SheetID2	varchar(20);
  declare @inShopName varchar(64);
  declare @outShopName varchar(64);
  
  declare @WholeRefSheetID varchar(16);
  declare @pxvenderid integer;

  set NOCOUNT on
  set XACT_ABORT on --分布式事务必须
  set ANSI_NULLS on --分布式事务必须
  set ANSI_WARNINGS on --分布式事务必须

  execute @Err=TL_GetMyShopID @MyShopID output, @HeadShopID output, @MyShopType output, @HeadShopType output;

  if @Err <> 0  or @Err is null
    begin
      select @Msg = '取本店号错误';
      goto ErrHandle;
    end;

  select @PalletZone = @MyShopID;

  --前后加逗号好判断
  select @NoSendWMSManage = ',' + Isnull(Value, '') + ','
  from   Config
  where  Name = '数据不传WMS部类';

  select @FreshWMSManage = ',' + Isnull(Value, '') + ','
  from   Config
  where  Name = '冷链仓库管理部类';

  select @CustomID = Value
  from   Config
  where  Name = '客户代码';

  --基本资料类，一次性处理当前清单中的所有资料到一张单转出
  --首先设置标志以免重入问题
  select @Notes = '';
  
  IF OBJECT_ID('tempdb..#sdwms_retration') IS NOT NULL DROP TABLE #sdwms_retration;
  CREATE TABLE #SDWMS_RetRATION
	(
	[Freshflag] [int] ,
	[SheetID] [varchar] (20) ,
	[CustomID] [varchar] (10) ,
	[PalletZone] [varchar] (10),
	[ShopID] [varchar] (10), 
	[LevelType] [int] ,
	[Sdate] [datetime],
	[SerialID] [int] IDENTITY(1,1),
	[GoodsID] [varchar] (20),
	[PKCount] [decimal] (12, 3),
	[PlanQty] [decimal] (12, 3),
	[BadFlag] [int],
	[ReasonTypeID] [int] ,
	[Venderid] [varchar] (20) ,
	[Cost] [decimal] (16, 6) ,
	[Notes] [varchar] (255) ,
	[Status] [int] NOT NULL ,
	[TaxRate] [decimal] (4, 2) NULL,
	[PayTypeID] [char] (2) ,	
	[PromFlag] [int] NULL,
	[reason] [CHAR](100) NULL,
	[shopname] [varchar] (64),
	[goodsname] [varchar] (64)
	) 

	if OBJECT_ID('Tempdb..#SDWMS_RationNote') is not null
	  Drop Table #SDWMS_RationNote;
	  
	CREATE TABLE #SDWMS_RationNote
	(
	[Freshflag] [int] NOT NULL,
	[Sheetid] [varchar] (20) ,
	[Refsheetid] [varchar] (20) ,
	[ShopID] [varchar] (10) ,
	[CustomID] [varchar] (10) ,
	[PalletZone] [varchar] (10) ,
	[Type] [int] NOT NULL ,
	[LevelType] [int] NOT NULL ,
	[Sdate] [datetime] NOT NULL,
	[Serialid] [int] identity(1,1),	
	[GoodsID] [varchar] (20) ,
	[Cost] [decimal] (16, 6) NOT NULL,
	[PlanQty] [decimal] (12, 3) NOT NULL,
	[GOODSTYPE] [int] NOT NULL,
	[Notes] [varchar] (255) ,
	[BadFlag] [int] NULL,
	[Status] [int] NOT NULL,
	[inShopName] [varchar] (64),				--入库门店名称
	[goodsname] [varchar] (64)					--商品名称
	)

  select @BreakPoint = 9721110;
  if @SheetType >= 1001 and @SheetType <= 1020
    begin
      exec @Err =Tl_getnewsheetid 33333,@NewSheetID output; --获得传输单据号
      if @sheetType=1001 
       update InterfaceSheetList0
       set    ExecuteFlag = 1
       from InterfaceSheetList0 a
       where  SheetType = @SheetType and InterfaceSystem = @InterfaceSystem and exists(select 1 from Cost where
       GoodsID=a.SheetID) and exists(select 1 from GoodsShop where GoodsID=a.SheetID)
     else
       update InterfaceSheetList0
      set    ExecuteFlag = 1
      where  SheetType = @SheetType and InterfaceSystem = @InterfaceSystem;
    end;

  --gzt 基础资料共用变量
  --管理部类的计算因子
  execute @LevelValue=Tl_managedeptgene; --TL_ManageDeptGene;
  if @LevelValue <= 0
    begin
      select @Err = 0;
      select @Msg = '产生管理部类的计算因子错误!';
      goto ErrHandle;
    end;

  select @WMSSheetID = FixString + @SheetID
  from   Sheet_PreFixString
  where  SheetType = @SheetType;

  select @pxvenderid = value from config where name = 'PX批销供应商'
  if @pxvenderid is null set @pxvenderid = -1;


  if @WMSSheetID is null
    select @WMSSheetID = @SheetID; --有些单据类型没有维护进来的，则用普通单号

  select @BreakPoint = 9721120;

  if @SheetType = 1001 --商品资料
  begin
      --基础资料直接一次就写入InterfaceSheetList0中SheetType等于1001的所有记录，清除的时候也是统一清除的
      --订货规格
      select @OrderPKNumType = OrderPKNumType
      from   ShopType
      where  ShopTypeID = 21;

      if Object_id('#GoodsPKNum') is not null
        drop table #GoodsPKNum

      create table #GoodsPKNum
        (
           GoodsID int not null,PKNum int default 1 not null,--订货数量
           PKName char(8) default '件' not null,--订货单位
           PKSpec char(32) default '1*1' not null, --订货规格
        );

      create index i1_TMP_GoodsPKNum
        on #GoodsPKNum(GoodsID);

      --最低级就是取主条码的件装数
      insert into #GoodsPKNum
      select a.GoodsID,a.PKNum,a.PKName,a.PKSpec
      from   Barcode a,InterfaceSheetList0 b
      where  a.Flag = 0 and a.GoodsID = b.SheetID and b.SheetType = @SheetType and b.ExecuteFlag = 1 and b.InterfaceSystem = @InterfaceSystem;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      --下一级是取流转吗的件装数
      update #GoodsPKNum
      set    PKNum = b.PKNum,
             PKName = b.PKName,
             PKSpec = b.PKSpec
      from   #GoodsPKNum a,Barcode b
      where  a.GoodsID = b.GoodsID and b.Flag = 1

      --最优先取的是GoodsPKNum有定义的
      if @OrderPKNumType = 1
        begin
          update #GoodsPKNum
          set    PKNum = b.OrderPKNum1,
                 PKName = b.OrderPKName1,
                 PKSpec = b.OrderPKSpec1
          from   #GoodsPKNum a,GoodsPKNum b
          where  a.GoodsID = b.GoodsID and DCShopID = @MyShopID;
        end
      else if @OrderPKNumType = 2
        begin
          update #GoodsPKNum
          set    PKNum = b.OrderPKNum2,
                 PKName = b.OrderPKName2,
                 PKSpec = b.OrderPKSpec2
          from   #GoodsPKNum a,GoodsPKNum b
          where  a.GoodsID = b.GoodsID and DCShopID = @MyShopID;
        end;
      else if @OrderPKNumType = 3
        begin
          update #GoodsPKNum
          set    PKNum = b.OrderPKNum3,
                 PKName = b.OrderPKName3,
                 PKSpec = b.OrderPKSpec3
          from   #GoodsPKNum a,GoodsPKNum b
          where  a.GoodsID = b.GoodsID and DCShopID = @MyShopID;
        end;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      select @BreakPoint = 9721130;

      select @DeptValue = levelvalue
      from   deptlevel
      where  deptlevelid = 1;

	  --清理原数据
	  delete from InterfaceWMSDB.dbo.SDWMS_Goods
	  where exists(select 1 from InterfaceSheetList0 b where b.sheetid = InterfaceWMSDB.dbo.SDWMS_Goods.goodsid and b.ExecuteFlag = 1 and b.sheettype = @SheetType and InterfaceSystem = @InterfaceSystem );

      --InterfaceSheetList0列中SheetID记录的为商品编码
      --SGroupID要求为末级分类，也就是小类了.....
      insert into InterfaceWMSDB.dbo.SDWMS_Goods
      (
        SheetID,GoodsID,Sgroupid,CustomID,BarcodeID,Name,EName,ABCID,UnitName,PKNAME,PKNUM,PKSpec,Spec,Length,Width,Height,Weigth,KeepDays,Price,Notes,SupNO,FreshDays,FreshFlag
      )
      select distinct SUBSTRING(@NewSheetID,5,12)+CAST(A.GOODSID AS CHAR(6)),a.GoodsID,a.DeptID/*CEILING(a.DeptID/@LevelValue)*/,@CustomID,a.BarcodeID,a.Name,a.Ename,'C',UnitName,Isnull(E.PKName, '件'),Isnull(E.PKNum, 1),Isnull(E.PKSpec, '1*1'),Isnull(a.Spec, '1*1'),--isnull(@PKName,'件'),isnull(@PKNum,0),1,isnull(@PKSpec,'1*1'),
                      0,0,0,0,Isnull(KeepDays, 0),Isnull(D.NormalPrice, 0),'',F.VenderID,A.FreshDays,0
      from   Goods A
	    inner join InterfaceSheetList0 B on a.GoodsID = b.SheetID and b.SheetType = @SheetType and b.ExecuteFlag = 1 
		inner join GoodsShop D on a.GoodsID = d.GoodsID and d.ShopID = @MyShopID
		inner join Cost F on a.GoodsID = F.GoodsID and F.Flag = 0
		left join #GoodsPKNum E on a.GoodsID = E.GoodsID
      where b.InterfaceSystem = @InterfaceSystem 
        and Charindex(',' + Cast(Ceiling(a.DeptID/@LevelValue) as varchar(50)) + ',', @NoSendWMSManage) = 0

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      select @BreakPoint = 9721131;

      select @FreshManageDeptID = Value
      from   Config
      where  Name = '冷链仓库管理部类';

      --更新生鲜标志....
     if @FreshManageDeptID<>'' 
      begin
       select @SQL = 'Update InterfaceWMSDB.dbo.SDWMS_Goods ' + ' Set FreshFlag=1 ' + ' From InterfaceWMSDB.dbo.SDWMS_Goods where Cast(CEILING(Sgroupid/'
                    + Cast(@LevelValue as varchar(20)) + ') as varchar(50)) in (' + @FreshManageDeptID + ') ';      

       exec(@SQL);
     end;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
  end
  else if @SheetType = 1002
  begin --供应商
      --InterfaceSheetList0列中SheetID记录的为供应商编码
      select @BreakPoint = 9721140;

	  --清理原数据
	  delete from InterfaceWMSDB.dbo.SDWMS_VENDER 
	  where exists(select 1 from InterfaceSheetList0 b where b.sheetid = InterfaceWMSDB.dbo.SDWMS_VENDER.venderid and b.ExecuteFlag = 1 and b.sheettype = @SheetType and InterfaceSystem = @InterfaceSystem );

      insert into InterfaceWMSDB.dbo.SDWMS_VENDER
      (
        SheetID,VENDERID,CustomID,Name,Manager,LinkMan,Address,zipcode,Tele,Fax,E_mail,Notes,OrderFlag
      )
      select distinct @NewSheetID,VenderID,@CustomID,Name,'',LinkMan,Address,ZipNo,TelNo,FaxNo,Email,'',a.WMSOrderFlag
      from   Vender a,InterfaceSheetList0 b
      where  a.VenderID = b.SheetID and b.SheetType = @SheetType and b.ExecuteFlag = 1 and InterfaceSystem = @InterfaceSystem;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
  end
  else if @SheetType = 1003
  begin --类别资料
      select @BreakPoint = 9721145;

	  --清理原数据
	  delete from InterfaceWMSDB.dbo.SDWMS_SGROUP 
	  where exists(select 1 from InterfaceSheetList0 b where b.sheetid = InterfaceWMSDB.dbo.SDWMS_SGROUP.Sgroupid and b.ExecuteFlag = 1 and b.sheettype = @SheetType and InterfaceSystem = @InterfaceSystem );

      --InterfaceSheetList0列中SheetID记录的为部类编码
      insert into InterfaceWMSDB.dbo.SDWMS_SGROUP
      (
        SheetID,Sgroupid,Sgroupname,CustomID
      )
      select distinct @NewSheetID,ID,Name,@CustomID
      from   SGroup a,InterfaceSheetList0 b
      where  a.ID = b.SheetID and b.SheetType = @SheetType and b.ExecuteFlag = 1 and InterfaceSystem = @InterfaceSystem;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      --加入同步小类
      select @BreakPoint = 9721145;

      --InterfaceSheetList0列中SheetID记录的为部类编码
      insert into InterfaceWMSDB.dbo.SDWMS_SGROUP
      (
        SheetID,Sgroupid,Sgroupname,CustomID
      )
      select distinct @NewSheetID,ID,Name,@CustomID
      from   Dept a,InterfaceSheetList0 b
      where  a.ID = b.SheetID and b.SheetType = @SheetType and b.ExecuteFlag = 1 and InterfaceSystem = @InterfaceSystem and a.ID <> 0;

      select @Err = @@error;
      if @Err != 0 goto ErrHandle;
  end
  else if @SheetType = 1004
  begin --机构资料,其中包含批发客户资料
      select @BreakPoint = 9721150;

	  --清理原数据
	  delete from InterfaceWMSDB.dbo.SDWMS_SHOP 
	  where exists(select 1 from InterfaceSheetList0 b where b.sheetid = InterfaceWMSDB.dbo.SDWMS_SHOP.shopid and b.ExecuteFlag = 1 and b.sheettype = @SheetType and InterfaceSystem = @InterfaceSystem );

      --机构资料
      --InterfaceSheetList0列中SheetID记录的为机构编码
      insert into InterfaceWMSDB.dbo.SDWMS_SHOP
      (
        SheetID,ShopID,CustomID,Name,ShopType,Manager,LinkMan,Address,zipcode,Tele,Fax,E_mail,Notes
      )
      select distinct @NewSheetID,ID,@CustomID,Name,ShopType,'','',Address,'',LinkTele,'','',''
      from   Shop a,InterfaceSheetList0 b
      where  a.ID = b.SheetID and b.SheetType = @SheetType and b.ExecuteFlag = 1 and InterfaceSystem = @InterfaceSystem;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
  end
  else if @SheetType = 1013
  begin
      select @BreakPoint = 9721151;

	  --清理原数据
	  delete from InterfaceWMSDB.dbo.SDWMS_SHOP
	  where exists(select 1 from InterfaceSheetList0 b where b.sheetid = InterfaceWMSDB.dbo.SDWMS_SHOP.shopid and b.ExecuteFlag = 1 and b.sheettype = @SheetType and InterfaceSystem = @InterfaceSystem );

      --批发客户资料
      --InterfaceSheetList0列中SheetID记录的为批发客户编码
      insert into InterfaceWMSDB.dbo.SDWMS_SHOP
      (
        SheetID,ShopID,CustomID,Name,ShopType,Manager,LinkMan,Address,zipcode,Tele,Fax,E_mail,Notes
      )
      select distinct @NewSheetID,ID,@CustomID,GuestName,88,'','','','',GuestTele,'',Email,''
      from   WholeGuest a,InterfaceSheetList0 b
      where  a.ID = b.SheetID and b.SheetType = @SheetType and b.ExecuteFlag = 1 and InterfaceSystem = @InterfaceSystem;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
  end
  else if @SheetType = 1005
  begin --多条码
      select @BreakPoint = 9721150;

	  --清理原数据
	  delete from InterfaceWMSDB.dbo.SDWMS_BARCODE
	  where exists(select 1 from InterfaceSheetList0 b where b.sheetid = InterfaceWMSDB.dbo.SDWMS_BARCODE .barcodeid and b.ExecuteFlag = 1 and b.sheettype = @SheetType and InterfaceSystem = @InterfaceSystem );

      --InterfaceSheetList0列中SheetID记录的为条码
      insert into InterfaceWMSDB.dbo.SDWMS_BARCODE
      (
        SheetID,Goodsid,CustomID,Barcodeid,SupNo,Primary_Flag,Status
      )
      select distinct SUBSTRING(@NewSheetID,5,12)+CAST(A.GOODSID AS CHAR(6)),a.Goodsid,@CustomID,Barcodeid,c.VenderID,case when a.Flag = 0 then '1' else '0' end as Primary_Flag,0
      from   barcode a,InterfaceSheetList0 b,Cost c
      where  a.barcodeid = b.SheetID and b.SheetType = @SheetType and b.ExecuteFlag = 1 and a.GoodsID = c.GoodsID and c.Flag = 0 and InterfaceSystem = @InterfaceSystem and Ltrim(Rtrim(a.barcodeid)) <> '' and a.barcodeid is not null;
      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
      --2211 验收通知单 	WMS_PURCHASE
      --2231 配送通知单	FR_RATIONNOTE
      --2252 退供应商通知单 	FR_RET
      --2253 反配退仓通知单 	FR_RetRATION
  end
  else if @SheetType=1006	--供应商商品信息
  begin
      select @BreakPoint = 9721151;

	  --清理原数据
	  delete from InterfaceWMSDB.dbo.SDwms_Purchase_Price
	  where exists(select 1 from InterfaceSheetList0 b where b.sheetid = InterfaceWMSDB.dbo.SDwms_Purchase_Price.goodsid and b.ExecuteFlag = 1 and b.sheettype = @SheetType and InterfaceSystem = @InterfaceSystem );

      insert into interfaceWMSDB.dbo.SDwms_Purchase_Price
        (SheetID,CustomID,GoodsID,VenderID,ShopID,Cost,TaxRate,ClearFlag,Flag)
      select distinct SUBSTRING(@NewSheetID,5,12)+CAST(A.GOODSID AS CHAR(6)),@CustomID,GoodsID,VenderID,ShopID,Cost,CostTaxRate,ClearFlag,Flag
      from Cost a,InterfaceSheetList0 b
      where a.goodsID=b.sheetid and b.sheettype=@SheetType and b.ExecuteFlag=1 and InterfaceSystem = @InterfaceSystem  and ShopID=@MyshopID 

      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

      --删除非冷链物流部类数据....
      Delete interfaceWMSDB.dbo.SDwms_Purchase_Price From interfaceWMSDB.dbo.SDwms_Purchase_Price a,Goods b
      where a.GoodsID=b.GoodsID and CHARINDEX(',' + Cast(CEILING(b.DeptID/@LevelValue) as varchar(50)) + ',', @FreshWMSManage) = 0

      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

  end 
  else if @SheetType=1007
  begin

      --清理原数据
	  delete from InterfaceWMSDB.dbo.SDwms_ShopPrice 
	  where exists(select 1 from InterfaceSheetList0 b where b.sheetid = InterfaceWMSDB.dbo.SDwms_ShopPrice.goodsid and b.ExecuteFlag = 1 and b.sheettype = @SheetType and InterfaceSystem = @InterfaceSystem );

      insert into interfaceWMSDB.dbo.SDwms_ShopPrice
        (SheetID,CustomID,GoodsID,ShopID,Price,EFTDate)
      select distinct SUBSTRING(@NewSheetID,5,12)+CAST(A.GOODSID AS CHAR(6)),@CustomID,GoodsID,ShopID,Price,Goodsdate 
      From GoodsShop a,InterfaceSheetList0 b
      where a.GoodsID=b.SheetID and b.SheetTYpe=@SheetType and b.ExecuteFlag=1 and InterfaceSystem=@InterFaceSystem and ShopID=@MyshopID

      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

      --删除非冷链物流部类数据....
      Delete interfaceWMSDB.dbo.SDwms_ShopPrice From interfaceWMSDB.dbo.SDwms_ShopPrice a,Goods b
      where a.GoodsID=b.GoodsID and CHARINDEX(',' + Cast(CEILING(b.DeptID/@LevelValue) as varchar(50)) + ',', @FreshWMSManage) = 0
  end;
  else if @SheetType = 2301
  begin --验收通知单 WMS_PURCHASE 验收单R
      --@Sdate 	采购单审核日期
      --@Type 	保管型=’0’、直通型=’1’默认为保管型
      --@purDate 	送货日期
      --@validDate 	本单据之有效日期，超过此日则失效
      select @BreakPoint = 9721170;

      --获取订单
      select @PurSheetID = PurSheetID
      from   ReceiptRef
      where  SheetID = @SheetID;

      select @Sdate = EditDate,@Logistics = Logistics,@ShopID = ShopID,@Notes = Notes,@ManageDeptID = ManageDeptID, @VenderId = venderid
      from   Receipt0
      where  SheetID = @SheetID;

      select @ValidDate = Purdate ,@PurDate = Purdate
      from   purchase
      where  SheetID = @PurSheetID;
      
      if @validDate is null 
      select @validDate=Purdate+Purday,@PurDate=Purdate from purchaseconfirm where SheetID=@PurSheetID;

      if @validDate is null
        select @ValidDate = Getdate() + 1;

      if @purDate is null
        select @purDate = Getdate() + 1;

      select @Type = 0;
      if @Logistics = 2 --直通小单收货生成的验收单
        begin
          select @Type = 1;
        end;

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      --0=正常订货 1=自采
      select @PurFlag = 0;
      select @PurFlag = case
                          when substring(RefSheetID, 1, 4) = @MyshopID then 1
                          else 0
                        end
      from   AskDataRef0
      where  RefSheetType = 2313 and SheetType = 2314 and SheetID = @PurSheetID
      if @PurFlag is null
        select @PurFlag = 0;

      insert into InterfaceWMSDB.dbo.SDWMS_PURCHASE
      (
        Sheetid,Freshflag,SheetType,PurFlag,GoodsID,CustomID,PalletZone,Venderid,
		Refsheetid,Sdate,Checker,Type,PurDate,ValidDate,
		Serialid,Pkcount,Qty,Cost,TaxRate,GiftQty,
		Notes,Status,vendername,goodsname
      )
      select @WMSSheetID,@FreshWMSFlag,(case when a.isadjust = 0 then 0 else 1 end) SheetType,@PurFlag,b.GoodsID,@CustomID,@PalletZone,a.VenderID,
	  a.RefSheetID,@SDate,a.Editor,@Type,@PurDate,@ValidDate,
	  case when subitem_iid = 0 then b.GoodsID else subitem_iid end as Serialid,b.PKNum,b.OrderQty + b.OrderPresentQty,b.Cost,b.TaxRate,b.OrderPresentQty,
	  b.Notes,0,c.Name,d.Name
      from   Receipt0 a,ReceiptItem0 b, Vender c, goods d
      where  a.sheetid = b.sheetid 
	  and a.VenderID = c.VenderID
	  and b.goodsid = d.GoodsID
	  and a.sheetid = @SheetID;

      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

      -------------------增加SerialID处理 Start-------------------------------------------------
      select @SerialID = 0;
      select @MaxSerialID = Max(SerialID)
      from   InterfaceWMSDB.dbo.SDWMS_PURCHASE
      where  SheetID = @WMSSheetID;
      if @MaxSerialID > 9999
        begin
          --游标重新更新SerialID
          declare Cur_UpdSerialID_1 cursor local for
            select SerialID
            from   InterfaceWMSDB.dbo.SDWMS_PURCHASE
            where  SheetID = @WMSSheetID
            for update
          open Cur_UpdSerialID_1
          while (1 = 1)
            begin
              if @@error != 0
                goto ErrHandle;
              fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
              if @@fetch_status != 0
                break;

              select @SerialID = @SerialID + 1;

              update InterfaceWMSDB.dbo.SDWMS_PURCHASE
              set    SerialID = @SerialID
              where  current of Cur_UpdSerialID_1

            end;
          close Cur_UpdSerialID_1;
          deallocate Cur_UpdSerialID_1;
        end;
      -------------------增加SerialID处理 End-------------------------------------------------
      select @Err = @@error;
      if @Err != 0
      goto ErrHandle;

	  ------------------增加批销处理-----------------------
	  if @pxvenderid <> -1 and @pxvenderid = @VenderId begin
	    insert into px_order (sheetid,sheettype,orderNo,shopNo,orderType,orderDate,deliveryDate,orderTitle,deliveryStatus)
		select @WMSSheetID,2301,@WMSSheetID,a.shopid,'Z001',@PurDate,@ValidDate,notes,-1
		from Receipt0 a
		where a.sheetid = @SheetID;

		insert into px_Order_Items (orderNo,goodscode,BarCode,GoodsId)
		select @WMSSheetID, b.goodscode, b.barcode, a.goodsid
		from ReceiptItem0 a
		  inner join px_goods_price b on a.goodsid = b.goodsid 
		where a.sheetid = @SheetID;
	  end;
  end;
  else if @SheetType = 2303 --直通验收分播单
  begin
      select @Sdate = EditDate,@Logistics = Logistics,@ShopID = ShopID,@Notes = Notes,@RefSheetID = RefSheetID,@Managedeptid = Managedeptid
      from   CRReceipt0
      where  SheetID = @SheetID;

      --直通大单收货必须从订货审批单中获取有效日期。
      select @ValidDate = Purdate ,@PurDate = Purdate
      from   PurchaseConfirm
      where  SheetID = @RefSheetID;

      if @validDate is null
        select @ValidDate = Getdate() + 1;

      if @purDate is null
        select @purDate = Getdate() + 1;

      select @Type = 1;

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      --0=正常订货 1=自采
      select @PurFlag = 0;

      insert into InterfaceWMSDB.dbo.SDWMS_PURCHASE
      (
        Sheetid,Freshflag,SheetType,PurFlag,GoodsID,CustomID,PalletZone,Venderid,Refsheetid,Sdate,Checker,Type,PurDate,ValidDate,
		Serialid,Pkcount,Qty,Cost,TaxRate,GiftQty,Notes,Status,vendername,goodsname
      )
      select @WMSSheetID,@FreshWMSFlag,0,@PurFlag,b.GoodsID,@CustomID,@PalletZone,a.VenderID,a.RefSheetID,@SDate,a.Editor,@Type,@PurDate,@ValidDate,
	  b.GoodsID as Serialid,b.PKNum,b.OrderQty + b.OrderPresentQty,b.Cost,b.TaxRate,b.OrderPresentQty,b.Notes,0,c.Name, d.name
      from   CRReceipt0 a,CRReceiptItem0 b,vender c,goods d
      where  a.Sheetid = b.Sheetid 
	  and a.venderid = c.VenderID
	  and b.goodsid = d.goodsid
	  and a.sheetid = @SheetID;

      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

      -------------------增加SerialID处理 Start-------------------------------------------------
      select @SerialID = 0;
      select @MaxSerialID = Max(SerialID)
      from   InterfaceWMSDB.dbo.SDWMS_PURCHASE
      where  SheetID = @WMSSheetID;
      if @MaxSerialID > 9999
        begin
          --游标重新更新SerialID
          declare Cur_UpdSerialID_1 cursor local for
            select SerialID
            from   InterfaceWMSDB.dbo.SDWMS_PURCHASE
            where  SheetID = @WMSSheetID
            for update
          open Cur_UpdSerialID_1
          while (1 = 1)
            begin
              if @@error != 0
                goto ErrHandle;
              fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
              if @@fetch_status != 0
                break;

              select @SerialID = @SerialID + 1;

              update InterfaceWMSDB.dbo.SDWMS_PURCHASE
              set    SerialID = @SerialID
              where  current of Cur_UpdSerialID_1

            end;
          close Cur_UpdSerialID_1;
          deallocate Cur_UpdSerialID_1;
        end;
    -------------------增加SerialID处理 End-------------------------------------------------

  end
  else if @SheetType = 2330
  begin --配送通知单
      select @BreakPoint = 9722300;

      select @SendDate = CheckDate + 1,@InShopID = InshopID,@ManageDeptID = DeptID,@Logistics = Logistics,@EmptyFlag = IsNull(IsAdjust, 0), @inShopName = b.name
      from   RationNote a, shop b 
      where  a.inshopid = b.id and SheetID = @SheetID;

      select @LevelType = 1; --1=正常 2=批发 9=虚拟

      --@Type 0=保管 1=直通
      if @Logistics = 2
        begin
          select @Type = 1;
          select @RefSheetID = RefSheetID
          from   RationNote
          where  sheetid = @SheetID;
          if @RefSheetID is null
            select @RefSheetID = ''
        end
      else
        begin
          select @Type = 0;
          select @RefSheetID = '';
        end;

      if @RefSheetID<>''
      begin
          select @Count=0;
          select @Count=Count(*) From CRReceipt0 where SheetID=@RefSheetID;
          if @Count=0
          begin
            select @Count=Count(*) From CRReceipt where SheetID=@RefSheetID;
          end;

          if @Count>0			--大直通
            select @Refsheetid = FixString + @Refsheetid
            from   Sheet_PreFixString
            where  SheetType = 2303
          else 				--小直通
            select @Refsheetid = FixString + @Refsheetid
            from   Sheet_PreFixString
            where  SheetType = 2301              
      end;

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      --跟WMS确认此表现在已经不再是一个sheetID只对应一个ShopID,会存在一个SheetID对应多个ShopID的情况....
      insert into InterfaceWMSDB.dbo.SDWMS_RationNote
      (
        Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,Serialid,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status, inShopName, goodsname
      )
      select @FreshWMSFlag,@WMSSheetID,@RefSheetID,@InShopID,@CustomID,@PalletZone,@Type,(case @EmptyFlag when 0 then 1 else 9 end) LevelType,@SendDate,a.GoodsID,a.GoodsID,C.Price,a.NotifyQty,0,Substring(a.Notes, 1, 255),case when a.BadFlag = 0 then 1 else 0 end,0,@inShopName,b.name
      from   RationNoteItem a,goods b,GoodsShop c
      where  a.goodsid = b.goodsid and a.SheetID = @SheetID and a.GoodsID = c.GoodsID and c.ShopID = @MyshopID

      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

      -------------------增加SerialID处理 Start-------------------------------------------------
      select @SerialID = 0;
      select @MaxSerialID = Max(SerialID)
      from   InterfaceWMSDB.dbo.SDWMS_RationNote
      where  SheetID = @WMSSheetID;
      if @MaxSerialID > 9999
        begin
          --游标重新更新SerialID
          declare Cur_UpdSerialID_1 cursor local for
            select SerialID
            from   InterfaceWMSDB.dbo.SDWMS_RationNote
            where  SheetID = @WMSSheetID
            for update
          open Cur_UpdSerialID_1
          while (1 = 1)
            begin
              if @@error != 0
                goto ErrHandle;
              fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
              if @@fetch_status != 0
                break;

              select @SerialID = @SerialID + 1;

              update InterfaceWMSDB.dbo.SDWMS_RationNote
              set    SerialID = @SerialID
              where  current of Cur_UpdSerialID_1

            end;
          close Cur_UpdSerialID_1;
          deallocate Cur_UpdSerialID_1;
        end;
    -------------------增加SerialID处理 End-------------------------------------------------
  end
  else if @SheetType = 2331 --批次配送
  begin
      select @BreakPoint = 9722310;

      select @ShopID = OutShopID,@SendDate = EditDate,@InShopID = InShopID,@Notes = Note,@ManageDeptID = ManageDeptID, @inshopname = b.Name
      from   Ration0 a, shop b
      where a.InShopID = b.id and  a.SheetID = @SheetID and a.RationType = 'I';

      select @SendDate = @SendDate + 1;

      select @levelType = 1
      select @Type = 0

      --暂时不启用,等WMS做好接口处理后再启用紧急功能
      --if @EMFlag=1 select @RType=9;

      --批次配送，没有源验收单号.....
      select @Refsheetid = ''
      /*select @Refsheetid = ReceiptSheetID
      from RationRef
      where SheetID = @SheetID --直通配送单关系表
      if @RefSheetID <> '' and ( not @RefSheetID is null )
      begin
        select @RType = 3; --直通配送

        --配送单的源验收单要加上 验收单的前缀，这样WMS才对得上
        select @Refsheetid = FixString + @Refsheetid
        from   Sheet_PreFixString
        where  SheetType = 2301;
      end else select @Refsheetid = '';*/

      select @BreakPoint = 9722313;

      --根据GoodsID汇总RationItem0的Qty,批次配送单是允许同一个商品多个批次的.....主键是SheetID,GoodsID,GoodsCostID
      if Object_id('tempdb..#RationItem') is not null
        drop table #RationItem

      select identity(int, 1, 1) as SerialID,SheetID,GoodsID,OutPrice,notifyQty,Notes,BadFlag
      into   #RationItem
      from   RationItem0
      where  1 = 0

      insert into #RationItem
      (
        SheetID,GoodsID,OutPrice,notifyQty,Notes,BadFlag
      )
      select SheetID,GoodsID,Max(OutPrice),Sum(NotifyQty),Max(Notes),Max(BadFlag)
      from   RationItem0
      where  SheetID = @SheetID
      group  by SheetID,GoodsID

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      insert into InterfaceWMSDB.dbo.SDWMS_RationNote
      (
        Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,Serialid,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status,inshopname,goodsname
      )
      select @FreshWMSFlag,@WMSSheetID,@RefSheetID,@InShopID,@CustomID,@PalletZone,@Type,@LevelType,@SendDate,SerialID,a.GoodsID,a.outPrice,a.NotifyQty,0,Substring(a.Notes, 1, 255),case when a.BadFlag = 0 then 1 else 0 end,0,@inshopname,b.Name
      from   #RationItem a,goods b
      where  a.goodsid = b.goodsid and a.SheetID = @SheetID

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      drop table #RationItem;
  end
  else if @SheetType = 2332 --返配单 SDWMS_RetRation
  begin
      select @ShopID = InShopID,@SendDate = EditDate,@OutShopID = OutShopID,@Notes = Note,@BSDate = EditDate,@ManageDeptID = ManageDeptID
      from   Ration0
      where  SheetID = @SheetID and RationType = 'O';

      select @BreakPoint = 9722531;

      --根据GoodsID汇总RationItem0的Qty,批次配送单是允许同一个商品多个批次的.....主键是SheetID,GoodsID,GoodsCostID
      if Object_id('tempdb..#RationItem2') is not null
        drop table #RationItem2

      select identity(int, 1, 1) as SerialID,SheetID,GoodsID,pknum,OutQty,Notes,ReasonTypeID,BadFlag,Reason
      into   #RationItem2
      from   RationItem0
      where  1 = 0

      insert into #RationItem2
      (
        SheetID,GoodsID,pknum,OutQty,Notes,ReasonTypeID,BadFlag,Reason
      )
      select SheetID,GoodsID,Min(pknum),Sum(OutQty),Max(Notes),Min(ReasonTypeID),Max(BadFlag),reason
      from   RationItem0 
      where  SheetID = @SheetID
      group  by SheetID,GoodsID,reason

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      select @BreakPoint = 9722532;

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      select @LevelType = 1; --1=正常 2=批发 9=虚拟

      select @count=count(*) from RationCostItem where sheetid=@Sheetid;
        if @Count=0
        begin
	 	select @Msg='配送记账单尚未送达，单据暂时不能下传....'
		goto ErrHandle;      
        end;

      --常温
      if @FreshWMSFlag=0
      BEGIN
        
        insert into InterfaceWMSDB.dbo.SDWMS_RetRATION
        (
          Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,SerialID,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
          PromFlag,shopname,goodsname,taxrate,PayTypeID
        )
        select @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,@OutShopID,@LevelType,@BSDate,ROW_NUMBER()  Over (ORDER BY B.goodsid) AS SerialID ,a.GoodsID,a.pknum,a.outQty,case when a.BadFlag = 0 then 1 else 0   end,a.ReasonTypeID,b.VenderID,b.Cost,a.Notes,0,
        b.promflag,d.Name,e.name,b.CostTaxRate,0
        from   #RationItem2 a,dbo.cost b,ration0 c, shop d,goods e
        where  a.SheetID = @SheetID AND b.flag=0 AND a.GoodsID = b.GoodsID 
		AND a.sheetid=c.sheetid 
		and c.OutShopID = d.id and b.goodsid = e.goodsid
		--AND c.managedeptid NOT IN (351,352)--and b.ShopID = @MyshopID and b.Flag = 0;
     /* insert into InterfaceWMSDB.dbo.SDWMS_RetRATION
        (
          Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,SerialID,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
          TaxRate,PromFlag,Reason
        )
        select @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,@OutShopID,@LevelType,@BSDate,ROW_NUMBER()  Over (ORDER BY a.goodsid) AS SerialID ,a.GoodsID,a.pknum,a.OutQty,case when a.BadFlag = 0 then 1 else 0   end,a.ReasonTypeID,999999,0,a.Notes,0,
        0,0,a.reason
        from   #RationItem2 a,ration0 b,RationCostItem c
        where  a.SheetID = @SheetID AND a.sheetid=c.sheetid AND a.sheetid=b.sheetid AND  b.managedeptid IN (351,352)--and b.ShopID = @MyshopID and b.Flag = 0;
   */
      end else begin	--生鲜单据
        insert into #SDWMS_RetRation--InterfaceWMSDB.dbo.SDWMS_RetRATION
        (
          Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
          TaxRate,PayTypeID,PromFlag,reason,shopname,goodsname
        )
        select @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,@OutShopID,@LevelType,@BSDate,a.goodsid,b.pknum,a.qty,case when b.BadFlag = 0 then 1 else 0 end,b.ReasonTypeID,a.VenderID,a.Cost,b.Notes,0,
          a.TaxRate,a.PayTypeID,a.PromFlag,b.reason,c.Name,d.name           
        from RationCostItem a,rationitem0 b, (select name from shop where id = @OutShopID) c,goods d
        where  a.SheetID = @SheetID and a.sheetid=b.sheetid 
		and a.goodsid = d.GoodsID
		and a.goodsid=b.goodsid

	INSERT INTO InterfaceWMSDB.dbo.SDWMS_RetRATION
        (
          Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,SerialID,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
          TaxRate,PayTypeID,PromFlag,shopname,goodsname
        )
        SELECT Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,SerialID,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
          TaxRate,PayTypeID,PromFlag,shopname,goodsname FROM #SDWMS_RetRation
       /* -------------------增加SerialID处理 Start-------------------------------------------------
        select @SerialID = 0;
       select @MaxSerialID = Max(SerialID)
        from   InterfaceWMSDB.dbo.SDWMS_RetRATION
        where  SheetID = @WMSSheetID;
        if @MaxSerialID > 9999
        begin
          --游标重新更新SerialID
          declare Cur_UpdSerialID_1 cursor local for
            select SerialID
            from   InterfaceWMSDB.dbo.SDWMS_RetRATION
            where  SheetID = @WMSSheetID order by SerialID
            for update
            
            
            
          open Cur_UpdSerialID_1
          while (1 = 1)
            begin
              if @@error != 0
                goto ErrHandle;
              fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
              if @@fetch_status != 0
                break;

              select @SerialID = @SerialID + 1;

              update InterfaceWMSDB.dbo.SDWMS_RetRATION
              set    SerialID = @SerialID
              where  current of Cur_UpdSerialID_1

            end;
          close Cur_UpdSerialID_1;
          deallocate Cur_UpdSerialID_1;
        end;*/
        -------------------增加SerialID处理 End-------------------------------------------------
      end;
      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

      drop table #RationItem2;
  END
  
  else if @SheetType = 2323 --退货单,批次退货单 SDWMS_RET
  begin
      select @BreakPoint = 9722520;

      select @ShopID = ShopId,@VenderID = VenderID,@ManageDeptID = ManageDeptID, @EmptyFlag = Isnull(IsAdjust,0)
      from   Ret0
      where  SheetId = @SheetID;

      if Object_id('tempdb..#RetItem') is not null
        drop table #RetItem;

      select SheetID as SheetID,GoodsID,PlanQty,Reason,subitem_iid,ReasonTypeID
      into   #RetItem
      from   Retitem0
      where  1 = 0;

      insert into #RetItem
      (
        SheetID,GoodsID,PlanQty,Reason,Subitem_iid,ReasonTypeID
      )
      select SheetID,GoodsID,Sum(PlanQty),Max(Reason),Max(Subitem_iid),Min(ReasonTypeID)
      from   Retitem0
      where  SheetID = @SheetID
      group  by SheetID,GoodsID

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      insert into InterfaceWMSDB.dbo.SDWMS_RET
      (
        Freshflag,SheetID,SheetType,CustomID,PalletZone,Venderid,Retdate,
		Serialid,GoodsID,Planqty,Notes,ReasonTypeID,
		BadFlag,Status,shopid,shopname,vendername,goodsname
      )
      select @FreshWMSFlag,@WMSSheetID,(case @EmptyFlag when 1 then 1 else 0 end) as SheetType,@CustomID,@PalletZone,a.VenderID,a.RetDate,
	  case Subitem_iid when 0 then       b.GoodsID else b.subitem_iid end as SerialID,b.GoodsID,b.PlanQty,b.Reason,b.ReasonTypeID,
	  case when a.BadFlag = 0 then 1 else 0 end,0,a.shopid,c.Name,d.Name, e.name
      from   Ret0 a,#RetItem b, shop c,vender d, goods e
      where  a.SheetID = b.SheetID and a.shopid = c.id and a.VenderID = d.VenderID and b.goodsid = e.goodsid
	  and a.SheetID = @SheetID and b.planqty <> 0;


      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

      -------------------增加SerialID处理 Start-------------------------------------------------
      select @SerialID = 0;
      select @MaxSerialID = Max(SerialID)
      from   InterfaceWMSDB.dbo.SDWMS_RET
      where  SheetID = @WMSSheetID;
      if @MaxSerialID > 9999
        begin
          --游标重新更新SerialID
          declare Cur_UpdSerialID_1 cursor local for
            select SerialID
            from   InterfaceWMSDB.dbo.SDWMS_RET
            where  SheetID = @WMSSheetID
            for update
          open Cur_UpdSerialID_1
          while (1 = 1)
            begin
              if @@error != 0
                goto ErrHandle;
              fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
              if @@fetch_status != 0
                break;

              select @SerialID = @SerialID + 1;

              update InterfaceWMSDB.dbo.SDWMS_RET
              set    SerialID = @SerialID
              where  current of Cur_UpdSerialID_1

            end;
          close Cur_UpdSerialID_1;
          deallocate Cur_UpdSerialID_1;
        end;
      -------------------增加SerialID处理 End-------------------------------------------------

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
  end
  else if @SheetType = 2413
  begin --报损单 WMS_RET
      select @BreakPoint = 9722760;

      select @ShopID = ShopID,@Notes = Note,@ManageDeptID = ManageDeptID
      from   Lost0
      where  SheetId = @SheetID;

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      --接口表SheetType填2代表报损数据.........
      insert into InterfaceWMSDB.dbo.SDWMS_RET
      (
        Freshflag,SheetID,SheetType,CustomID,PalletZone,Venderid,Retdate,Serialid,GoodsID,Planqty,Notes,ReasonTypeID,Status,BadFlag
      )
      select @FreshWMSFlag,@WMSSheetID,2,@CustomID,@PalletZone,888888,getdate(),case SerialID when 0 then b.GoodsID else b.SerialID end as SerialID,b.GoodsID,b.CheckQty,b.Remask,0,0,0
      from   Lost0 a,LostItem0 b
      where  a.SheetID = b.SheetID and a.SheetID = @SheetID and b.CheckQty <> 0;

      -------------------增加SerialID处理 Start-------------------------------------------------
      select @SerialID = 0;
      select @MaxSerialID = Max(SerialID)
      from   InterfaceWMSDB.dbo.SDWMS_RET
      where  SheetID = @WMSSheetID;
      if @MaxSerialID > 9999
        begin
          --游标重新更新SerialID
          declare Cur_UpdSerialID_1 cursor local for
            select SerialID
            from   InterfaceWMSDB.dbo.SDWMS_RET
            where  SheetID = @WMSSheetID
            for update
          open Cur_UpdSerialID_1
          while (1 = 1)
            begin
              if @@error != 0
                goto ErrHandle;
              fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
              if @@fetch_status != 0
                break;

              select @SerialID = @SerialID + 1;

              update InterfaceWMSDB.dbo.SDWMS_RET
              set    SerialID = @SerialID
              where  current of Cur_UpdSerialID_1

            end;
          close Cur_UpdSerialID_1;
          deallocate Cur_UpdSerialID_1;
        end;
      -------------------增加SerialID处理 End-------------------------------------------------

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
  end
  else if @SheetType = 2441
  begin --盘点 WMS_PD
      select @BreakPoint = 9722730;

      select @PDFlag = PDFlag,@ShopID = ShopID
      from   PD0
      where  SheetID = @SheetID;

      if @PDFlag = 9
        begin
          insert into InterfaceWMSDB.dbo.SDWMS_PD
          (
            SheetID,CustomID,PalletZone,PDType,Goodsid,Sdate,Status
          )
          select @WMSSheetID,@CustomID,@PalletZone,1,'0',RunDate,0
          from   PD0
          where  SheetID = @SheetID;
        end
      else
        begin
          insert into InterfaceWMSDB.dbo.SDWMS_PD
          (
            SheetID,CustomID,PalletZone,PDType,Goodsid,Sdate,Status
          )
          select distinct @WMSSheetID,@CustomID,@PalletZone,2,b.GoodsID,a.RunDate,0
          from   PD0 a,PDGoodsItem0 b,Goods c
          where  a.SheetID = @SheetID and a.SheetID = b.SheetID and b.GoodsID = c.GoodsID and Charindex(',' + Cast(Ceiling(c.DeptID/@LevelValue) as varchar(50)) + ',', @NoSendWMSManage) = 0 --不传不进WMS部类的盘点通知
        end;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
  end
  else if @SheetType = 2451
  begin --批发出库和批发退货  接口表: WMS_RationCustom WMS_RATIONNOTE WMS_RetRATION
      select @ShopID = ShopID,@RetFlag = RetFlag,@ManageDeptID = ManageDeptID
      from   WholeSale0
      where  SheetID = @SheetID;
      

      if @RetFlag = 0
        begin
          select @BadFlag = BadFlag,@ManageDeptID = ManageDeptID
          from   WholeSale0
          where  SheetID = @SheetID;

          --写批发商资料
          insert into InterfaceWMSDB.dbo.SDWMS_RationCustom
          (
            SheetID,CustomID,GuestID,GuestName,Address,Zipcode,Tele,Status
          )
          select @WMSSheetID,@CustomID,a.GuestID,b.GuestName,'',b.PostalCode,b.GuestTele,0
          from   WholeSale0 a,WholeGuest b
          where  a.GuestID = b.ID and a.SheetID = @SheetID and a.RetFlag = 0

          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;

          --0=常温 1=低温
          if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
            begin
              select @FreshWMSFlag = 1;
            end
          else
            begin
              select @FreshWMSFlag = 0;
            end;

          select @Type = 0;
          select @levelType = 2;

          --写批发出库资料,批发时ShopID即为批发客户编码
          insert into InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
          (
            Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,Serialid,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status
          )
          select @FreshWMSFlag,@WMSSheetID,'',a.GuestID,@CustomID,@PalletZone,@Type,@LevelType,GetDate(),Case when b.SerialID=0 then b.GoodsID else b.SerialID end as SerialID,b.GoodsID,b.Price,
          CASE when b.Qty!=0 then b.qty WHEN b.wholeqty<>0 THEN b.wholeqty ELSE b.PlanQty end as qty,0,Substring(b.Notes, 1, 255),case when @BadFlag = 0 then 1 else 0 end,0
          from   WholeSale0 a,WholeSaleItem0 b
          where  a.SheetID = @SheetID and a.SheetID = b.SheetID and a.RetFlag = 0

          select @Err = @@error;
          if @Err != 0 goto ErrHandle;

          -------------------增加SerialID处理 Start-------------------------------------------------
          select @SerialID = 0;
          select @MaxSerialID = Max(SerialID)
          from   InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
          where  SheetID = @WMSSheetID;
          if @MaxSerialID > 9999
            begin
              --游标重新更新SerialID
              declare Cur_UpdSerialID_1 cursor local for
                select SerialID
                from   InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
                where  SheetID = @WMSSheetID
                for update
              open Cur_UpdSerialID_1
              while (1 = 1)
                begin
                  if @@error != 0
                    goto ErrHandle;
                  fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
                  if @@fetch_status != 0
                    break;

                  select @SerialID = @SerialID + 1;

                  update InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
                  set    SerialID = @SerialID
                  where  current of Cur_UpdSerialID_1

                end;
              close Cur_UpdSerialID_1;
              deallocate Cur_UpdSerialID_1;
            end;
          -------------------增加SerialID处理 End-------------------------------------------------

          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;
        end
      else if @RetFlag = 1 --批发退货
        begin
          select @BadFlag = BadFlag,@ManageDeptID = ManageDeptID
          from   WholeSale0
          where  SheetID = @SheetID;

          --0=常温 1=低温
          if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
            begin
              select @FreshWMSFlag = 1;
            end
          else
            begin
              select @FreshWMSFlag = 0;
            end;

          select @LevelType = 2; --1=正常 2=批发 9=虚拟
        select @WholeRefSheetID=RetsheetID from retwholeref where SheetID=@SheetID;
      	if @WholeRefSheetID is null select @WholeRefSheetID=''; 

         if @WholeRefSheetID<>''  --批发退仓单
         begin
           insert into #SDWMS_RetRATION--InterfaceWMSDB.dbo.SDWMS_RetRATION
           (
            Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
            TaxRate,PayTypeID,PromFlag,shopname,goodsname
           )
            Select  @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,a.GuestID,@LevelType,GetDate(),        
			b.goodsID,c.pknum,b.Qty,0,0,b.DCVenderID,isnull(b.dccost,-1),c.notes,0,b.taxrate,v.paytypeid,d.promflag,e.Name,f.name
			from WholeSale0 A, RetCostItem B,(select goodsid,notes,pknum,min(price) price          
				from WholeSaleItem0 where sheetid =@SheetID group by goodsid,notes,pknum ) C,cost d,vender v,
				shop e,goods f 
			where A.SheetID=@SheetID and B.SheetID=@WholeRefSheetID and B.goodsid=C.goodsid and b.GoodsID=d.GoodsID and d.ShopID=@MyShopID
			 and (d.VenderID=b.DCVenderID ) and d.VenderID=v.VenderID 
			 and a.GuestID = e.id and b.goodsid = f.goodsid
			 and B.dcvenderid is not null;
			 
			/*Select  @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,a.GuestID,@LevelType,GetDate(),isnull(b.subitem_iid,1),         
			b.goodsID,c.pknum,b.Qty,0,0,d.VenderID,d.Cost,c.notes,0,d.CostTaxRate,v.paytypeid,d.promflag			     
			from WholeSale0 A, RetCostItem B,(select goodsid,notes,pknum,min(price) price           
				from WholeSaleItem0 where sheetid =@SheetID group by goodsid,notes,pknum ) C,cost d,vender v          
			where A.SheetID=@SheetID and B.SheetID=@WholeRefSheetID and B.goodsid=C.goodsid and b.GoodsID=d.GoodsID and d.ShopID=@MyShopID
			 and d.Flag=0 and d.VenderID=v.VenderID and B.dcvenderid is null; */
		end	 
        else  
	     insert into #SDWMS_RetRATION--InterfaceWMSDB.dbo.SDWMS_RetRATION
          (
            Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
            TaxRate,PayTypeID,PromFlag,shopname,goodsname
          )
          select @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,a.GuestID,@LevelType,GetDate(),b.GoodsID,b.PKNum,
		  case when b.PlanQty=0 then b.qty else           b.PlanQty end as qty,case when a.BadFlag = 0 then 1 else 0 end,0,c.VenderID,
		  c.Cost,b.Notes,0,c.costTaxRate,d.PayTypeID,c.promflag,e.Name,f.name
           from   WholeSale0 a,WholeSaleItem0 b,cost c,vender d, shop e,goods f
          where  a.SheetID = @SheetID and a.SheetID = b.SheetID and a.RetFlag = 1 and b.GoodsID=c.GoodsID and c.VenderID=d.VenderID
		  and a.GuestID = e.id and b.goodsid = f.GoodsID
          and c.flag=0 and c.shopid=@myshopid;

          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;  
        INSERT INTO InterfaceWMSDB.dbo.SDWMS_RetRATION
          (
            Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
            TaxRate,PayTypeID,PromFlag,SerialID,shopname,goodsname
          )
        SELECT Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,GoodsID,PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
            TaxRate,PayTypeID,PromFlag,SerialID,shopname,goodsname FROM #SDWMS_RetRATION       
        end;
  end
  else if @SheetType = 2450 --批发通知单
  begin --接口表: WMS_RationCustom WMS_RATIONNOTE WMS_RetRATION
      select @ShopID = ShopID,@RetFlag = RetFlag,@ManageDeptID = ManageDeptID
      from WholeSaleNote0
      where SheetID = @SheetID;

      if @RetFlag = 0
        begin
          select @BadFlag = BadFlag,@ManageDeptID = ManageDeptID
          from WholeSaleNote0
          where SheetID = @SheetID;

          --写批发商资料
          insert into InterfaceWMSDB.dbo.SDWMS_RationCustom
          (SheetID,CustomID,GuestID,GuestName,Address,Zipcode,Tele,Status)
          select @WMSSheetID,@CustomID,a.GuestID,b.GuestName,'',b.PostalCode,b.GuestTele,0
          from WholeSaleNote0 a,WholeGuest b
          where a.GuestID = b.ID and a.SheetID = @SheetID and a.RetFlag = 0

          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;

          --0=常温 1=低温
          if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
            begin
              select @FreshWMSFlag = 1;
            end
          else
            begin
              select @FreshWMSFlag = 0;
            end;

          select @Type = 0;
          select @levelType = 8;

          --写批发出库资料,批发时ShopID即为批发客户编码
          insert into InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
          (Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,
           Sdate,Serialid,
           GoodsID,Cost,PlanQty,GoodsType,Notes,
           BadFlag,Status)
          select @FreshWMSFlag,@WMSSheetID,'',a.GuestID,@CustomID,@PalletZone,@Type,@LevelType,
            GetDate(),Case when b.SerialID=0 then b.GoodsID else b.SerialID end as SerialID,
            b.GoodsID,b.Price,case when b.WholeQty!=0 then b.WholeQty else b.Qty end,0,Substring(b.Notes, 1, 255),
            case when @BadFlag = 0 then 1 else 0 end,0
          from WholeSaleNote0 a,WholeSaleNoteItem0 b
          where a.SheetID = @SheetID and a.SheetID = b.SheetID and a.RetFlag = 0

          select @Err = @@error;
          if @Err != 0 goto ErrHandle;

          -------------------增加SerialID处理 Start-------------------------------------------------
          select @SerialID = 0;
          select @MaxSerialID = Max(SerialID)
          from InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
          where SheetID = @WMSSheetID;
          if @MaxSerialID > 9999
            begin
              --游标重新更新SerialID
              declare Cur_UpdSerialID_1 cursor local for
                select SerialID
                from InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
                where SheetID = @WMSSheetID
                for update
              open Cur_UpdSerialID_1
              while (1 = 1)
                begin
                  if @@error != 0
                    goto ErrHandle;
                  fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
                  if @@fetch_status != 0
                    break;

                  select @SerialID = @SerialID + 1;

                  update InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
                  set    SerialID = @SerialID
                  where  current of Cur_UpdSerialID_1

                end;
              close Cur_UpdSerialID_1;
              deallocate Cur_UpdSerialID_1;
            end;
          -------------------增加SerialID处理 End-------------------------------------------------

          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;
        end
      else if @RetFlag = 1 --批发退货
        begin
          select @BadFlag = BadFlag,@ManageDeptID = ManageDeptID
          from   WholeSaleNote0
          where  SheetID = @SheetID;

          --0=常温 1=低温
          if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
            begin
              select @FreshWMSFlag = 1;
            end
          else
            begin
              select @FreshWMSFlag = 0;
            end;

          select @LevelType = 8; --1=正常 2=批发 9=虚拟

	     insert into InterfaceWMSDB.dbo.SDWMS_RetRATION
         (Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,SerialID,GoodsID,
          PKCount,PlanQty,BadFlag,ReasonTypeID,Venderid,
          Cost,Notes,Status,TaxRate,PayTypeID,PromFlag,shopname,goodsname)
          select @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,a.GuestID,@LevelType,GetDate(),b.SerialID,b.GoodsID,
            b.PKNum,case when b.WholeQty!=0 then b.WholeQty else b.Qty end,case when a.BadFlag = 0 then 1 else 0 end,0,
            c.VenderID,c.Cost,b.Notes,0,c.costTaxRate,d.PayTypeID,c.promflag, e.Name, f.name
          from WholeSaleNote0 a,WholeSaleNoteItem0 b,cost c,vender d,shop e,goods f
          where a.SheetID = @SheetID and a.SheetID = b.SheetID and a.RetFlag = 1 
            and b.GoodsID=c.GoodsID and c.VenderID=d.VenderID and c.flag=0 
			and a.GuestID = e.ID and b.goodsid = f.GoodsID
			and c.shopid=@myshopid;

          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;

          -------------------增加SerialID处理 Start-------------------------------------------------
          select @SerialID = 0;
          select @MaxSerialID = Max(SerialID)
          from   InterfaceWMSDB.dbo.SDWMS_RetRATION
          where  SheetID = @WMSSheetID;
          if @MaxSerialID > 9999
            begin
              --游标重新更新SerialID
              declare Cur_UpdSerialID_1 cursor local for
                select SerialID
                from   InterfaceWMSDB.dbo.SDWMS_RetRATION
                where  SheetID = @WMSSheetID
                for update
              open Cur_UpdSerialID_1
              while (1 = 1)
                begin
                  if @@error != 0
                    goto ErrHandle;
                  fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
                  if @@fetch_status != 0
                    break;

                  select @SerialID = @SerialID + 1;

                  update InterfaceWMSDB.dbo.SDWMS_RetRATION
                  set    SerialID = @SerialID
                  where  current of Cur_UpdSerialID_1

                end;
              close Cur_UpdSerialID_1;
              deallocate Cur_UpdSerialID_1;
            end;
          -------------------增加SerialID处理 End-------------------------------------------------

          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;
        end;
  end
  else if @SheetType = 2315
  begin --订单完结
      --制单控制生鲜和非生鲜的订货审批单不能存在于一张订货审批单有效期调整单里面...
      --随便取一个部类作为生鲜和非生鲜的区别，从上游制单里面强制区分开生鲜部类和非生鲜部类...

      select @ManageDeptID=ManageDeptID from PurchaseOverItem where SheetID=@SheetID and ManageDeptID is not null;
      
      if @ManageDeptID is null
      begin
        select @Msg = '订货审批单有效期调整单的表体不允许出现没有部类编码的记录，请调整部类编码....';
        goto ErrHandle;
      end;

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      declare Cur_PurchaseOver cursor local for
        select PurSheetID,Purdate,DelFlag
        from   PurchaseOverItem
        where  SheetID = @SheetID
      open Cur_PurchaseOver
      while (1 = 1)
        begin
          if @@error != 0 goto ErrHandle;

          Fetch NEXT from Cur_PurchaseOver into @PurSheetID, @Purdate, @DelFlag

          if @@fetch_status != 0
            break;

          if @DelFlag=1 select @DelFlag=0 else select @DelFlag=1;

          insert into InterfaceWMSDB.dbo.SDWMS_PurOver
          (
            Freshflag,Sheetid,PurSheetID,Flag,Purdate,Status
          )
          values
          (
            @FreshWMSFlag,@WMSSheetID,@PurSheetID,@Delflag,@Purdate,0
          )
          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;
        end;
      close Cur_PurchaseOver
      deallocate Cur_PurchaseOver

  end
  else if @SheetType = 2423 --行政领用
  begin
      select @ManageDeptID = ManageDeptID,@RetFlag=DirectFlag
      from   XTran0
      where  SheetID = @SheetID;

      --0=常温 1=低温
      if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
        begin
          select @FreshWMSFlag = 1;
        end
      else
        begin
          select @FreshWMSFlag = 0;
        end;

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;

      select @Type = 0;
      select @levelType = 3;

     if @RetFlag=0  --正常领用
     begin
      --写行政领用出库资料,批发时ShopID即为批发客户编码
      insert into InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
      (
        Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,Serialid,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status
      )
      select @FreshWMSFlag,@WMSSheetID,'',@MyshopID,@CustomID,@PalletZone,@Type,@LevelType,GetDate(),b.GoodsID,b.GoodsID,b.Price,b.CheckQty,0,'',0,0
      from   XTran0 a,XTranItem0 b
      where  a.SheetID = @SheetID and a.SheetID = b.SheetID

      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

      -------------------增加SerialID处理 Start-------------------------------------------------
      select @SerialID = 0;
      select @MaxSerialID = Max(SerialID)
      from   InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
      where  SheetID = @WMSSheetID;
      if @MaxSerialID > 9999
        begin
          --游标重新更新SerialID
          declare Cur_UpdSerialID_1 cursor local for
            select SerialID
            from   InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
            where  SheetID = @WMSSheetID
            for update
          open Cur_UpdSerialID_1
          while (1 = 1)
            begin
              if @@error != 0
                goto ErrHandle;
              fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
              if @@fetch_status != 0
                break;

              select @SerialID = @SerialID + 1;

              update InterfaceWMSDB.dbo.SDWMS_RATIONNOTE
              set    SerialID = @SerialID
              where  current of Cur_UpdSerialID_1

            end;
          close Cur_UpdSerialID_1;
          deallocate Cur_UpdSerialID_1;
        end;
       end else
       if @RetFlag=1  --领用退货
       begin
         insert into InterfaceWMSDB.dbo.SDWMS_RetRATION
          (
            Freshflag,SheetID,CustomID,PalletZone,ShopID,LevelType,Sdate,SerialID,GoodsID,PKCount,
            PlanQty,BadFlag,ReasonTypeID,Venderid,Cost,Notes,Status,
            TaxRate,PayTypeID,PromFlag
          )
          select @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,a.shopid,@LevelType,GetDate(),b.SubItem_IID,b.GoodsID,1,
           b.checkqty,0,0,c.VenderID,c.Cost,'行政领用退货',0,c.costTaxRate,d.PayTypeID,c.promflag
           from   xtran0 a,xtranitem0 b,cost c,vender d
          where  a.SheetID = @SheetID and a.SheetID = b.SheetID and a.directflag = 1 and b.GoodsID=c.GoodsID and c.VenderID=d.VenderID
          and c.flag=0 and c.shopid=@myshopid;

          select @Err = @@error;
          if @Err != 0
            goto ErrHandle;

          -------------------增加SerialID处理 Start-------------------------------------------------
          select @SerialID = 0;
          select @MaxSerialID = Max(SerialID)
          from   InterfaceWMSDB.dbo.SDWMS_RetRATION
          where  SheetID = @WMSSheetID;
          if @MaxSerialID > 9999
            begin
              --游标重新更新SerialID
              declare Cur_UpdSerialID_1 cursor local for
                select SerialID
                from   InterfaceWMSDB.dbo.SDWMS_RetRATION
                where  SheetID = @WMSSheetID
                for update
              open Cur_UpdSerialID_1
              while (1 = 1)
                begin
                  if @@error != 0
                    goto ErrHandle;
                  fetch NEXT from Cur_UpdSerialID_1 into @MaxSerialID;
                  if @@fetch_status != 0
                    break;

                  select @SerialID = @SerialID + 1;

                  update InterfaceWMSDB.dbo.SDWMS_RetRATION
                  set    SerialID = @SerialID
                  where  current of Cur_UpdSerialID_1

                end;
              close Cur_UpdSerialID_1;
              deallocate Cur_UpdSerialID_1;
            end;
          -------------------增加SerialID处理 End-------------------------------------------------

         end;
      
      -------------------增加SerialID处理 End-------------------------------------------------

      select @Err = @@error;
      if @Err != 0
        goto ErrHandle;
  end
  else if @SheetType=2431 		--库存进价调整单
  begin
     select @ManageDeptID = ManageDeptID,@Notes=Note,@VenderID=VenderID from UpdCostValue
     where  SheetID = @SheetID;

     --0=常温 1=低温
     if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
     begin
          select @FreshWMSFlag = 1;
     end
     else
     begin
          select @FreshWMSFlag = 0;
     end;
    
     Insert into InterfaceWMSDB.dbo.SDwms_ChangeCost
       (Freshflag,SheetID,CustomID,PalletZone,GoodsID,VenderID,LotNO,Cost,Notes)
     select @FreshWMSFlag,@WMSSheetID,@CustomID,@PalletZone,GoodsID,@VenderID,GoodsCostID,NewCost,@Notes
     From UpdCostValueItem where SheetID=@SheetID;
  
      select @Err = @@error;
      if @Err != 0 goto ErrHandle;

  end else if @SheetType=2341  --调拨通知单
  begin
    select @managedeptid=managedeptid,@outshopid=outshopid,@SendDate = CheckDate + 1,@InShopID=inshopid, @inshopname = b.name from transfernote0 a, shop b where a.inshopid = b.id and a.sheetid=@SheetID;
      --0=常温 1=低温
     if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
     begin
          select @FreshWMSFlag = 1;
     end
     else
     begin
          select @FreshWMSFlag = 0;
     end;
     select @Type=0;
    
    insert into #SDWMS_RationNote-- InterfaceWMSDB.dbo.SDWMS_RationNote
      (
        Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status,inShopName,goodsname
      )
     select @FreshWMSFlag,@WMSSheetID,@WMSSheetID,@inShopID,@CustomID,@PalletZone,@Type,1 LevelType,@SendDate,a.GoodsID,C.Price,a.NotifyQty,0,Substring(a.Notes, 1, 255),0,0,@inshopname,b.name
      from TransferNoteItem0 a,goods b,GoodsShop c
      where  a.goodsid = b.goodsid and a.SheetID = @SheetID and a.GoodsID = c.GoodsID and c.ShopID = @MyshopID;
     select @Err=@@Error; if @Err!=0 goto ErrHandle;
     
     insert into InterfaceWMSDB.dbo.SDWMS_RationNote
      (
        Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status,Serialid,inShopName,goodsname
      )
      select Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status,Serialid,@inshopname,goodsname
      from #SDWMS_RationNote;
     
     if OBJECT_ID('tempdb..#SDWMS_RationNote') is not null Drop table #SDWMS_RationNote;
      
  end else if @SheetType=2342  --调拨出库单
  begin
    select @managedeptid=managedeptid,@outshopid=outshopid,@SendDate = EditDate + 1, @outshopname = b.name from transfer0 a, shop b where a.outshopid = b.id and sheetid=@SheetID;
      --0=常温 1=低温
     if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
     begin
          select @FreshWMSFlag = 1;
     end
     else
     begin
          select @FreshWMSFlag = 0;
     end;
     select @Type=0;
    
    insert into  #SDWMS_RationNote  --InterfaceWMSDB.dbo.SDWMS_RationNote
      (
        Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status,inshopname,goodsname
      )
     select @FreshWMSFlag,@WMSSheetID,@WMSSheetID,@OutShopID,@CustomID,@PalletZone,@Type,1 LevelType,@SendDate,a.GoodsID,C.Price,a.NotifyQty,0,Substring(a.Notes, 1, 255),0,0,@outshopname,b.Name
      from TransferItem0 a,goods b,GoodsShop c
      where  a.goodsid = b.goodsid and a.SheetID = @SheetID and a.GoodsID = c.GoodsID and c.ShopID = @MyshopID;
     select @Err=@@Error; if @Err!=0 goto ErrHandle;
     
     insert into InterfaceWMSDB.dbo.SDWMS_RationNote
      (
        Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status,Serialid,inshopname,goodsname
      )
      select Freshflag,Sheetid,Refsheetid,ShopID,CustomID,PalletZone,Type,LevelType,Sdate,GoodsID,Cost,PlanQty,GoodsType,Notes,BadFlag,Status,Serialid,inshopname,goodsname
      from #SDWMS_RationNote;
      select @Err=@@ERROR; if @Err!=0 goto ErrHandle;
     
     if OBJECT_ID('tempdb..#SDWMS_RationNote') is not null Drop table #SDWMS_RationNote;
     select @Err=@@ERROR; if @Err!=0 goto ErrHandle;
  end else if @SheetType=2344  --调拨入库单
  begin
    select @count=count(*) from TransferCostItem where sheetid=@Sheetid;
        if @Count=0
        begin
	 	select @Msg='配送记账单尚未送达，单据暂时不能下传....'
		goto ErrHandle;      
        end;
    select @managedeptid=managedeptid,@InShopID=InShopID,@SendDate = CheckDate + 1,@SDate=EditDate from transfer0 where sheetid=@SheetID;
      --0=常温 1=低温
     if CHARINDEX(',' + Cast(@ManageDeptID as varchar(50)) + ',', @FreshWMSManage) > 0
     begin
       select @FreshWMSFlag = 1;
     end
     else
     begin
       select @FreshWMSFlag = 0;
     end;
     select @Type=0;
    select @ValidDate=@SendDate+100;
    insert into InterfaceWMSDB.dbo.SDWMS_PURCHASE
      (Sheetid,Freshflag,SheetType,PurFlag,GoodsID,CustomID,PalletZone,Venderid,Refsheetid,Sdate,Checker,Type,PurDate,ValidDate,
	  Serialid,Pkcount,Qty,Cost,TaxRate,GiftQty,Notes,Status,vendername,goodsname)
    select @WMSSheetID,@FreshWMSFlag,0,0,b.GoodsID,@CustomID,@PalletZone,a.OutShopID,a.NoteSheetID,@SDate,a.Editor,@Type,100,@ValidDate,
	  b.serialid,b.PKNum,sum(c.Qty),sum(c.Cost*c.Qty)/SUM(c.Qty),c.TaxRate,0,b.Notes,0,d.name,e.name
     from Transfer0 a,TransferItem0 b,TransferCostItem c,shop d, goods e
     where  a.sheetid = b.sheetid and a.sheetid=c.sheetid and b.goodsid=c.goodsid 
	 and a.OutShopID = d.id and b.goodsid = e.goodsid
	 and a.sheetid = @SheetID 
     group by b.goodsid,a.OutShopID,a.SheetID,a.Editor,b.serialid,b.PKNum,c.TaxRate,b.Notes,a.NoteSheetID,d.Name,e.name having SUM(c.Qty)<>0;
     select @Err=@@Error; if @Err!=0 goto ErrHandle; 
  end;

  --基本资料类，使用传单单据号代替原单据号，且备份处理过的资料接口
  --基本资料与WMS定义，使用总部店号代表所有配送仓系统都需要更新
  select @BreakPoint = 9721230;

  if @SheetType >= 1001 and @SheetType <= 1020
    begin
      insert into InterfaceSheetList
      select * from InterfaceSheetList0
      where  SheetType = @SheetType and InterfaceSystem = @InterfaceSystem and ExecuteFlag = 60;

      delete from InterfaceSheetList0
      where  SheetType = @SheetType and InterfaceSystem = @InterfaceSystem and ExecuteFlag = 60;

      select @SheetID = @NewSheetID;

      select @ShopID = ID
      from   Shop
      where  ShopType = 0;
    end;

  select @Out_ShopID = @ShopID;
  select @Out_SheetID = @SheetID;
IF OBJECT_ID('tempdb..#sdwms_retration') IS NOT NULL DROP TABLE #sdwms_retration;

  return 0;

ErrHandle:
  raiserror('%s,断点=%d,Err=%d',16,1,@Msg,@BreakPoint,@Err);
  return -1;
End;

GO
