package com.douzone.server.config.utils;

/**
 * 컨트롤러,필터 성공/실패 MSG
 */
public interface Msg {
	// spring security, interceptor
	String SUCCESS_SIGN_IN = "로그인에 성공하였습니다.";
	String SUCCESS_SIGN_OUT = "로그아웃에 성공하였습니다.";
	String SUCCESS_TOKEN_VALIDATE = "토큰 검증에 성공하였습니다.";
	String SUCCESS_USER_ROLE = "유저 역할 확인에 성공하였습니다.";
	String SUCCESS_ACCESS = "서버 접근에 성공하였습니다.";

	String FAIL_SIGN_IN = "로그인에 실패하였습니다.";
	String FAIL_SIGN_OUT = "로그아웃에 실패하였습니다.";
	String FAIL_TOKEN_VALIDATE = "토큰 검증에 실패하였습니다.";
	String FAIL_USER_ROLE = "유저 역할 확인에 실패하였습니다.";
	String FAIL_ACCESS = "서버 접근에 실패하였습니다.";

	// admin
	String SUCCESS_ADMIN_REGISTER = "관리자가 사원 등록에 성공하였습니다.";
	String SUCCESS_ADMIN_MOD = "관리자가 사원 수정에 성공하였습니다.";
	String SUCCESS_ADMIN_MODPW = "관리자가 사원 비밀번호 수정에 성공하였습니다.";
	String SUCCESS_ADMIN_FIND_ALL = "사원 전체 예약 현황 조회에 성공하였습니다.";


	String FAIL_ADMIN_REGISTER = "관리자가 사원 등록에 실패하였습니다.";
	String FAIL_ADMIN_FIND_ALL = "사원 전체 예약 현황 조회에 실패하였습니다.";

	// employee
	String SUCCESS_EMP_PROFILE = "사원 프로필 조회에 성공하였습니다.";

	String FAIL_EMP_PROFILE = "사원 프로필 조회에 실패하였습니다.";

	// room
	String SUCCESS_ROOM_RESERVE = "회의실 예약에 성공하였습니다.";
	String SUCCESS_ROOM = "회의실 등록에 성공하였습니다.";
	String SUCCESS_ROOM_DELETE = "회의실 삭제에 성공하였습니다.";
	String SUCCESS_ROOM_FIND_ALL = "회의실 예약 현황 전체 조회에 성공하였습니다.";
	String SUCCESS_ROOM_FIND_NO = "호수별 회의실 예약 조회에 성공하였습니다.";
	String SUCCESS_ROOM_FIND_DATE = "특정 시간대 회의실 예약 조회에 성공하였습니다.";
	String SUCCESS_ROOM_BEST_MARK = "즐겨찾기된 회의실 TOP3 조회에 성공하였습니다.";
	String SUCCESS_ROOM_RECENT = "최근 예약된 회의실 조회에 성공하였습니다.";
	String SUCCESS_ROOM_SOON_MY_TIME = "나의 다가오는 회의실 시간 및 남은 회의 시간 조회에 성공하였습니다.";
	String SUCCESS_ROOM_RESERVE_MY = "나의 예약 현황 조회에 성공하였습니다.";
	String SUCCESS_ROOM_RESERVE_EMP = "해당 회원 예약 현황 조회에 성공하였습니다.";
	String SUCCESS_ROOM_BEST_WEEK = "일주일간 많이 예약된 회의실 개수 조회에 성공하였습니다.";
	String SUCCESS_ROOM_BEST_DATE = "일주일간 많이 예약하는 시간대 회의실 조회에 성공하였습니다.";
	String SUCCESS_ROOM_MEET_START = "일주일간 많이 회의가 시작되는 시간대 회의실 조회에 성공하였습니다.";
	String SUCCESS_ROOM_BEFORE = "사용 후 회의실 예약 기록 조회에 성공하였습니다.";
	String SUCCESS_ROOM_AFTER = "사용 전 회의실 예약 내역 조회에 성공하였습니다.";
	String SUCCESS_ROOM_FIND_MARK = "내가 즐겨찾기한 회의실 조회에 성공하였습니다.";
	String SUCCESS_ROOM_UPDATE = "회의실 예약 정보 수정에 성공하였습니다.";
	String SUCCESS_ROOM_RESERVATION_DELETE = "회의실 예약 삭제에 성공하였습니다.";
	String SUCCESS_ROOM_DELETE_MARK = "회의실 즐겨찾기 삭제에 성공하였습니다.";
	String SUCCESS_ROOM_BOOKMARK = "회의실 즐겨찾기 등록에 성공하였습니다.";

	String FAIL_ROOM_RESERVE = "회의실 예약에 실패하였습니다.";
	String FAIL_ROOM = "회의실 등록에 성공하였습니다.";
	String FAIL_ROOM_DELETE = "회의실 삭제에 성공하였습니다.";
	String FAIL_ROOM_BOOKMARK = "회의실 즐겨찾기 등록에 실패하였습니다.";
	String FAIL_ROOM_FIND_ALL = "회의실 예약 현황 전체 조회에 실패하였습니다.";
	String FAIL_ROOM_SOON_MY_TIME = "나의 다가오는 회의실 시간 및 남은 회의 시간 조회에 실패하였습니다.";
	String FAIL_ROOM_RESERVE_MY = "나의 예약 현황 조회에 성공하였습니다.";
	String FAIL_ROOM_RESERVE_EMP = "나의 예약 현황 조회에 성공하였습니다.";
	String FAIL_ROOM_FIND_NO = "호수별 회의실 예약 조회에 실패하였습니다.";
	String FAIL_ROOM_FIND_DATE = "특정 시간대 회의실 예약 조회에 실패하였습니다.";
	String FAIL_ROOM_BEST_MARK = "즐겨찾기된 회의실 TOP3 조회에 실패하였습니다.";
	String FAIL_ROOM_RECENT = "최근 예약된 회의실 조회에 실패하였습니다.";
	String FAIL_ROOM_BEST_WEEK = "일주일간 가장 많이 예약된 회의실 조회에 실패하였습니다.";
	String FAIL_ROOM_BEST_DATE = "일주일간 가장 많이 예약하는 시간대 조회에 실패하였습니다.";
	String FAIL_ROOM_MEET_START = "일주일간 많이 회의가 시작되는 시간대 회의실 조회에 성공하였습니다.";
	String FAIL_ROOM_BEFORE = "사용 후 회의실 예약 기록 조회에 실패하였습니다.";
	String FAIL_ROOM_AFTER = "사용 전 회의실 예약 내역 조회에 실패하였습니다.";
	String FAIL_ROOM_FIND_MARK = "내가 즐겨찾기한 회의실 조회에 실패하였습니다.";
	String FAIL_ROOM_UPDATE = "회의실 예약 정보 수정에 실패하였습니다.";
	String FAIL_ROOM_DELETE_RESERVATION = "회의실 예약 삭제에 실패하였습니다.";
	String FAIL_ROOM_DELETE_MARK = "회의실 즐겨찾기 삭제에 실패하였습니다.";

	// vehicle
	String SUCCESS_VEHICLE_RESERVE = "차량 예약에 성공하였습니다.";
	String SUCCESS_VEHICLE_BOOKMARK = "차량 즐겨찾기 등록에 성공하였습니다.";
	String SUCCESS_VEHICLE_FIND_ALL = "차량 예약 현황 전체 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_FIND_NONE = "차량 미예약 현황 전체 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_FIND_TYPE = "차량 종류에 따른 예약 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_FIND_DATE = "특정 시간대 차량 예약 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_BEST_MARK = "즐겨찾기된 차량 TOP3 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_RECENT = "최근 예약된 차량 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_BEST_WEEK = "일주일간 가장 많이 예약된 차량 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_BEST_DATE = "일주일간 가장 많이 예약한 시간대 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_BEFORE = "사용 후 차량 예약 기록 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_AFTER = "사용 전 차량 예약 내역 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_FIND_MARK = "내가 즐겨찾기한 차량 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_UPDATE = "차량 예약 정보 수정에 성공하였습니다.";
	String SUCCESS_VEHICLE_DELETE = "차량 예약 삭제에 성공하였습니다.";
	String SUCCESS_VEHICLE_DELETE_MARK = "차량 즐겨찾기 삭제에 성공하였습니다.";
	String SUCCESS_VEHICLE_FIND_NO = "차량 예약 현황 단일 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_RESISTER = "차량 정보 등록에 성공하였습니다.";
	String SUCCESS_VEHICLE_INFO_UPDATE = "차량 정보 수정에 성공하였습니다.";
	String SUCCESS_VEHICLE_INFO_DELETE = "차량 정보 삭제에 성공하였습니다.";
	String SUCCESS_VEHICLE_SOON = "다음 예약 시작 시간 조회에 성공하였습니다.";
	String SUCCESS_VEHICLE_ING = "현재 예약 종료 시간 조회에 성공하였습니다.";

	String FAIL_VEHICLE_RESERVE = "차량 예약에 실패하였습니다.";
	String FAIL_VEHICLE_BOOKMARK = "차량 즐겨찾기 등록에 실패하였습니다.";
	String FAIL_VEHICLE_FIND_ALL = "차량 예약 현황 전체 조회에 실패하였습니다.";
	String FAIL_VEHICLE_FIND_NONE = "차량 미예약 현황 전체 조회에 실패하였습니다.";
	String FAIL_VEHICLE_FIND_TYPE = "차량 종류에 따른 예약 조회에 실패하였습니다.";
	String FAIL_VEHICLE_FIND_DATE = "특정 시간대 차량 예약 조회에 실패하였습니다.";
	String FAIL_VEHICLE_BEST_MARK = "즐겨찾기된 차량 TOP3 조회에 실패하였습니다.";
	String FAIL_VEHICLE_RECENT = "최근 예약된 차량 조회에 실패하였습니다.";
	String FAIL_VEHICLE_BEST_WEEK = "일주일간 가장 많이 예약된 차량 조회에 실패하였습니다.";
	String FAIL_VEHICLE_BEST_DATE = "일주일간 가장 많이 예약한 시간대 조회에 실패하였습니다.";
	String FAIL_VEHICLE_BEFORE = "사용 후 차량 예약 기록 조회에 실패하였습니다.";
	String FAIL_VEHICLE_AFTER = "사용 전 차량 예약 내역 조회에 실패하였습니다.";
	String FAIL_VEHICLE_FIND_MARK = "내가 즐겨찾기한 차량 조회에 실패하였습니다.";
	String FAIL_VEHICLE_UPDATE = "차량 예약 정보 수정에 실패하였습니다.";
	String FAIL_VEHICLE_DELETE = "차량 예약 삭제에 실패하였습니다.";
	String FAIL_VEHICLE_DELETE_MARK = "차량 즐겨찾기 삭제에 실패하였습니다.";
	String FAIL_VEHICLE_FIND_NO = "차량 예약 현황 단일 조회에 실패하였습니다.";
	String FAIL_VEHICLE_RESISTER = "차량 정보 등록에 실패하였습니다.";
	String FAIL_VEHICLE_INFO_UPDATE = "차량 정보 수정에 실패하였습니다.";
	String FAIL_VEHICLE_INFO_DELETE = "차량 정보 삭제에 실패하였습니다.";
	String FAIL_VEHICLE_SOON = "다음 예약 시작 시간 조회에 실패하였습니다.";
	String FAIL_VEHICLE_ING = "현재 예약 종료 시간 조회에 실패하였습니다.";
	String FAIL_FIND_RESULT = "결과값 조회에 실패하였습니다.";
	String FAIL_EXIST_RESULT = "결과값이 존재하지 않습니다.";
	String FAIL_REQUEST_PARAMETER = "잘못된 파라미터가 전달되었습니다";
	public static final String SUCCESS_ADMIN_PROFILEIMG = "관리자가 사원 프로필 이미지 등록에 성공하였습니다.";
	public static final String FAIL_ADMIN_PROFILEIMG = "관리자가 사원 프로필 이미지 등록 실패하였습니다.";
}
