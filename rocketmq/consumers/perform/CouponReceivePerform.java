package cn.vpclub.promotion.coupon.provider.consumers.perform;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;

import cn.vpclub.promotion.coupon.api.CouponCycleBatchService;
import cn.vpclub.promotion.coupon.api.domain.Coupon;
import cn.vpclub.promotion.coupon.api.domain.CouponCode;
import cn.vpclub.promotion.coupon.api.domain.CouponStatistics;
import cn.vpclub.promotion.coupon.api.model.redis.CouponRedisKey;
import cn.vpclub.promotion.coupon.common.utils.ByteUtils;
import cn.vpclub.promotion.coupon.provider.consumers.ConsumersPerform;


@Component("couponReceive")
public class CouponReceivePerform implements ConsumersPerform<CouponCode> {

	
	private static  final Logger  logger = LoggerFactory.getLogger( CouponReceivePerform.class );
	
	
	public static final List<Coupon> getCouponList(List<CouponCode> couponCodeList , Map<String , Coupon> couponMap){
		List<Coupon> list = new ArrayList<>( couponCodeList.size( ) );
		int i = 0 , size = couponCodeList.size( );
		CouponCode couponCode;
		Set< String > set = new HashSet<>( );
		for( ; ;){
			couponCode = couponCodeList.get( i++ );
			if( set.add( couponCode.getCouponId( ) ) )
	        	list.add( couponMap.get( couponCode.getCouponId( ) ));		
			if( i >= size ){
        		return list;
        	}	        	
        }
	}
	
	
	public static final  List<Coupon> couponSort(List<Coupon> couponList){
        Collections.sort(couponList, new Comparator<Coupon>() {
            @Override
            public int compare(Coupon o1, Coupon o2) {
                return o1.getId().compareTo( o2.getId());
            }
        });
        return  null;
    }
	
	public static final  List<CouponStatistics> getCouponStatistics(List<Coupon> couponList , Map<String , CouponStatistics> couponStatisticsMap){
        List<CouponStatistics> list = new ArrayList<>( couponList.size( ) );
        int i = 0 , size = couponList.size( );
        Coupon coupon;
        for( ; ;){
        	coupon = couponList.get( i++ );
        	list.add( couponStatisticsMap.get( coupon.getId( ) ));
        	if( i >= size ){
        		return list;
        	}
        }
    }
	
	
	
	@Autowired
	@Qualifier("couponCycleConsumersServiceImpl")
	private CouponCycleBatchService couponCycleBatchService;
	
	@Autowired
	private RedisConnectionFactory redisConnection;
	
	private Map<String , Coupon> couponMap = new ConcurrentHashMap<>( );
	
	private Map<String , CouponStatistics> couponStatisticsMap = new ConcurrentHashMap<>( );
	
	private Actuator actuator = new Actuator();
	
	private Thread thread;
	
	public CouponReceivePerform(){
		Thread thread = new Thread( actuator );
		thread.setName( "actuator" );
		thread.start( );
	}
	

	@Override
	public Class< CouponCode > getType( ) {
		return CouponCode.class;
	}

	/**
	 * 这里可以在优化下
	 * 有点复杂，
	 */
	@Override
	public boolean perform( List< CouponCode > list ) {
		long time = System.currentTimeMillis( );
		RedisConnection con = null;
		try{
			Map<String , Coupon> couponMap = new HashMap<>( );
			combination( list ,couponMap);		
			List<Coupon> couponList = getCouponList( list , couponMap );
			couponSort( couponList );	
			
			Request request = new Request(couponList);
			actuator.putRequest( request );
			couponCycleBatchService.recelve( list , null , null );
			logger.info( String.format( "执行 recelve 方法时间为 ： %d" , System.currentTimeMillis( ) - time ) );

			time = System.currentTimeMillis( );
			byte[] by;
			con = redisConnection.getConnection( );
			con.openPipeline( );
			for(CouponCode cc : list){
	            by = ByteUtils.addBytes(CouponRedisKey.COUPON_USER_BYTE_KEY, cc.getReceiveUser().getBytes());
	            con.hSet(by , cc.getId().getBytes() , JSON.toJSONBytes( cc ));
	        }
			List<Object> listObject = con.closePipeline( );
			logger.info("closePipeline usr time: {}  , return data : {}" ,System.currentTimeMillis( ) - time , listObject.toString( ) );
			return true;
		}catch (Exception e) {
			logger.error( e.getMessage( ) , e );
			return false;
		}finally{
			if( con != null)
				con.close( );
		}
	}

	
	public void combination(List< CouponCode > couponCodeList ,Map<String , Coupon> couponMap){
		try{
			CouponCode couponCode;Coupon coupon;CouponStatistics cs = null;
			int i = 0 , msgListSize = couponCodeList.size( );
			for ( ; ; ) {
				couponCode = couponCodeList.get( i++ );
				coupon = couponMap.get( couponCode.getCouponId( ) );				
				if ( coupon == null ) {
					coupon = new Coupon();
					coupon.setId( couponCode.getCouponId( ) );
					coupon.setRemainMoney( 0 );
					coupon.setRemainQuantity( 0 );
					couponMap.put( coupon.getId( ) , coupon );
					cs = new CouponStatistics();
		            cs.setId( coupon.getId() );
		            cs.setRemainQuantity( 0 );
			        cs.setReceiveTotalAmount( 0 );			
		            //couponStatisticsMap.put( coupon.getId( ) , cs );
				}else{
					//couponStatisticsMap.get( couponCode.getCouponId( ) );
				}
		        coupon.setRemainMoney( coupon.getRemainMoney( ) + couponCode.getAmount());						       
		        coupon.setRemainQuantity( coupon.getRemainQuantity( ) + 1 );
		      /*  cs.setRemainQuantity( cs.getReceiveQuantity( ) +  1);
		        cs.setReceiveTotalAmount( cs.getReceiveTotalAmount( ) + couponCode.getAmount() );	*/							
				if ( i >= msgListSize ) {
					i = 0;
					break;
				}
			}
		}finally{
			
		}
	}


	@Override
	public TypeReference< List< CouponCode > > getTypeReference( ) {
		return  new TypeReference<List<CouponCode>>() {};
	}


	@Override
	public void combination( List< CouponCode > list ) {
		
	}
	
	
	class Actuator implements Runnable{
		
		private volatile LinkedBlockingDeque<Request> requestsWrite = new LinkedBlockingDeque<Request>();

		private volatile boolean isStop = false;
		
        public  void putRequest(final Request request) {
                this.requestsWrite.add(request);           
        }
		
        
        
		@Override
		public void run( ) {
			logger.info( String.format( "actuator is start %s , thread name  is " , this.getClass( ).getName( ) , Thread.currentThread( ).getName( )) );
			Request request =null;
			List< Coupon > couponList = null;
			while( !isStop )
				try{
					while(!isStop || requestsWrite.isEmpty( )){
						request = requestsWrite.take( );
						long time = System.currentTimeMillis( );
						couponList = request.getCouponList( );
						if( couponList == null){
							continue;							
						}
						couponCycleBatchService.recelve( null , couponList , null );
						logger.info( String.format( "actuator 执行 recelve 方法时间为 ： %d" , System.currentTimeMillis( ) - time ) );
						request.wakeupCustomer( true );
					}					
				}catch (Exception e) {
					if( request != null )
						request.wakeupCustomer(false);
					logger.error("the exec failure data :　" + JSON.toJSONString( couponList ) );
					logger.error( e.getMessage( ) ,e );
				} 
			
		}
		
		public void isStop(){
			isStop = true;
		}
		
	}
	
	class Request{
		private final List<Coupon> couponList;
        private final CountDownLatch countDownLatch = new CountDownLatch(1);
        private volatile boolean flushOK = false;

        public Request(List<Coupon> couponList) {
            this.couponList = couponList;
        }

        public List<Coupon> getCouponList(){
        	return this.couponList;
        }
        
        public void wakeupCustomer(final boolean flushOK) {
            this.flushOK = flushOK;
            this.countDownLatch.countDown();
        }

        public boolean waitForFlush(long timeout) {
            try {
                this.countDownLatch.await(timeout, TimeUnit.MILLISECONDS);
                return this.flushOK;
            } catch (InterruptedException e) {
                e.printStackTrace();
                return false;
            }
        }
	}

	@Override
	public boolean finish( ) {
		actuator.isStop( );
		Request request = new Request(null);
		actuator.putRequest( request );
		request.waitForFlush(2000);
		return false;
	}
	
}
