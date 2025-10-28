package it.korea.app_bmpc.store.service;

import java.io.File;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.korea.app_bmpc.common.dto.PageVO;
import it.korea.app_bmpc.common.utils.FileUtils;
import it.korea.app_bmpc.store.dto.CategoryDTO;
import it.korea.app_bmpc.store.dto.StoreDTO;
import it.korea.app_bmpc.store.dto.StoreFileDTO;
import it.korea.app_bmpc.store.dto.StoreSearchDTO;
import it.korea.app_bmpc.store.entity.StoreCategoryEntity;
import it.korea.app_bmpc.store.entity.StoreEntity;
import it.korea.app_bmpc.store.entity.StoreFileEntity;
import it.korea.app_bmpc.store.entity.StoreHourEntity;
import it.korea.app_bmpc.store.repository.CategoryRepository;
import it.korea.app_bmpc.store.repository.StoreRepository;
import it.korea.app_bmpc.store.repository.StoreSearchSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
@RequiredArgsConstructor
public class StoreService {

    @Value("${server.file.store.path}")
    private String filePath;

    private final StoreRepository storeRepository;
    private final CategoryRepository categoryRepository;
    private final FileUtils fileUtils;

    /**
     * 가게 카테고리 리스트 가져오기 
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public List<CategoryDTO> getCategoryList() throws Exception {
        return categoryRepository.findAll().stream().map(CategoryDTO::of).toList();
    }

    /**
     * 가게 리스트 가져오기
     * @param pageable 페이징 객체
     * @param searchDTO 검색 객체
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public Map<String, Object> getStoreList(Pageable pageable, StoreSearchDTO searchDTO) throws Exception {
        Map<String, Object> resultMap = new HashMap<>();

        Page<StoreEntity> pageList = null;

        StoreSearchSpecification searchSpecification = new StoreSearchSpecification(searchDTO);
        pageList = storeRepository.findAll(searchSpecification, pageable);

        List<StoreDTO.Response> storeList = pageList.getContent().stream().map(StoreDTO.Response::of).toList();

        PageVO pageVO = new PageVO();
        pageVO.setData(pageList.getNumber(), (int) pageList.getTotalElements());

        resultMap.put("total", pageList.getTotalElements());
        resultMap.put("content", storeList);
        resultMap.put("pageHTML", pageVO.pageHTML());
        resultMap.put("page", pageList.getNumber());
        
        return resultMap;
    }

    /**
     * 가게 상세정보 가져오기
     * @param storeId 가게 아이디
     * @return
     * @throws Exception
     */
    @Transactional(readOnly = true)
    public StoreDTO.Detail getStore(int storeId) throws Exception {
        return StoreDTO.Detail.of(storeRepository.getStore(storeId)
            .orElseThrow(()-> new RuntimeException("해당 가게가 존재하지 않습니다.")));
    }

    /**
     * 가게 등록
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    public void createStore(StoreDTO.Request request) throws Exception {
        
        MultipartFile mainImage = request.getMainImage();

        if (mainImage == null || mainImage.isEmpty()) {   // 메인 이미지가 없거나 빈 파일일 경우
            throw new RuntimeException("메인 이미지는 필수입니다.");
        }

        StoreEntity entity = new StoreEntity();
        entity.setStoreName(request.getStoreName());
        entity.setBranchName(request.getBranchName());
        entity.setPhone(request.getPhone());
        entity.setAddr(request.getAddr());
        entity.setAddrDetail(request.getAddrDetail());
        entity.setRatingAvg(BigDecimal.ZERO);
        entity.setReviewCount(0);
        entity.setMinPrice(request.getMinPrice());
        entity.setOrigin(request.getOrigin());
        entity.setNotice(request.getNotice());
        entity.setDelYn("N");

        // 카테고리 매핑
        for (Integer caId : request.getCategoryIds()) {
            StoreCategoryEntity sc = new StoreCategoryEntity();
            sc.setCategory(categoryRepository.findById(caId)
                .orElseThrow(()-> new RuntimeException("해당 카테고리가 존재하지 않습니다.")));

            entity.addCategory(sc, false);
        }

        // 영업시간 매핑
        for (StoreDTO.HourDTO hourDto : request.getHourList()) {
            StoreHourEntity hourEntity = new StoreHourEntity();
            hourEntity.setDayOfWeek(hourDto.getDayOfWeek());
            hourEntity.setOpenTime(hourDto.getOpenTime());
            hourEntity.setCloseTime(hourDto.getCloseTime());
            hourEntity.setCloseYn(hourDto.getCloseYn());

            entity.addHour(hourEntity, false);
        }

        // 메인 이미지 파일 업로드
        Map<String, Object> mainImageMap = fileUtils.uploadImageFiles(request.getMainImage(), filePath);


        // 메인 이미지 파일이 있을 경우에만 파일 엔티티 생성
        if (mainImageMap != null) {  
            StoreFileEntity fileEntity = new StoreFileEntity();
            fileEntity.setFileName(mainImageMap.get("fileName").toString());
            fileEntity.setStoredName(mainImageMap.get("storedFileName").toString());
            fileEntity.setFilePath(mainImageMap.get("filePath").toString());
            fileEntity.setFileThumbName(mainImageMap.get("thumbName").toString());
            fileEntity.setFileSize(request.getMainImage().getSize());
            fileEntity.setMainYn("Y");

            entity.addFiles(fileEntity, false);  // 가게 엔티티와 파일 엔티티 관계를 맺어줌
        }

        List<MultipartFile> imageList = request.getImage();

        if (imageList != null && imageList.size() > 0) {
            for (MultipartFile image : imageList) {
                if (image != null && !image.isEmpty()) {
                    // 기타 이미지 파일 업로드
                    Map<String, Object> imageMap = fileUtils.uploadImageFiles(image, filePath);

                    // 기타 이미지 파일이 있을 경우에만 파일 엔티티 생성
                    if (imageMap != null) {
                        StoreFileEntity fileEntity = new StoreFileEntity();
                        fileEntity.setFileName(imageMap.get("fileName").toString());
                        fileEntity.setStoredName(imageMap.get("storedFileName").toString());
                        fileEntity.setFilePath(imageMap.get("filePath").toString());
                        fileEntity.setFileThumbName(imageMap.get("thumbName").toString());
                        fileEntity.setFileSize(image.getSize());
                        fileEntity.setMainYn("N");
                        
                        entity.addFiles(fileEntity, false);  // 가게 엔티티와 파일 엔티티 관계를 맺어줌
                    }
                }
            }
        }

        storeRepository.save(entity);
    }

    /**
     * 가게 수정
     * @param request
     * @return
     * @throws Exception
     */
    @Transactional
    public void updateStore(StoreDTO.Request request) throws Exception {

        // 1. 수정하기 위해 기존 정보를 불러온다 
        StoreEntity entity = storeRepository.getStore(request.getStoreId())   
            .orElseThrow(()-> new RuntimeException("해당 가게가 존재하지 않습니다."));

        StoreDTO.Detail detail = StoreDTO.Detail.of(entity);

        entity.setStoreName(request.getStoreName());
        entity.setBranchName(request.getBranchName());
        entity.setPhone(request.getPhone());
        entity.setAddr(request.getAddr());
        entity.setAddrDetail(request.getAddrDetail());
        entity.setMinPrice(request.getMinPrice());
        entity.setOrigin(request.getOrigin());
        entity.setNotice(request.getNotice());

        // 카테고리 수정
        entity.getCategoryList().clear(); // 기존 카테고리 전부 삭제
        for (Integer caId : request.getCategoryIds()) {
            StoreCategoryEntity sc = new StoreCategoryEntity();
            sc.setCategory(categoryRepository.findById(caId)
                .orElseThrow(() -> new RuntimeException("해당 카테고리가 존재하지 않습니다.")));
            entity.addCategory(sc, true);
        }

        // 영업시간 수정
        entity.getHourList().clear(); // 기존 영업시간 초기화
        for (StoreDTO.HourDTO hourDto : request.getHourList()) {
            StoreHourEntity hourEntity = new StoreHourEntity();
            hourEntity.setDayOfWeek(hourDto.getDayOfWeek());
            hourEntity.setOpenTime(hourDto.getOpenTime());
            hourEntity.setCloseTime(hourDto.getCloseTime());
            hourEntity.setCloseYn(hourDto.getCloseYn());

            entity.addHour(hourEntity, true);
        }

        // 2. 업로드 할 메인 이미지 파일이 있으면 업로드
        if (request.getMainImage() != null && !request.getMainImage().isEmpty()) {
            // 2-1. 파일 업로드
            Map<String, Object> fileMap = fileUtils.uploadImageFiles(request.getMainImage(), filePath);   // 파일 업로드 과정 공통화해서 분리

            entity.getFileList().removeIf(file -> "Y".equals(file.getMainYn()));  // 메인 이미지만 삭제
            
            // 2-2. 파일 등록
            // 파일이 있을 경우에만 파일 엔티티 생성
            if (fileMap != null) {  
                StoreFileEntity fileEntity = new StoreFileEntity();
                fileEntity.setFileName(fileMap.get("fileName").toString());
                fileEntity.setStoredName(fileMap.get("storedFileName").toString());
                fileEntity.setFilePath(fileMap.get("filePath").toString());
                fileEntity.setFileThumbName(fileMap.get("thumbName").toString());
                fileEntity.setFileSize(request.getMainImage().getSize());
                fileEntity.setMainYn("Y");

                // 파일만 수정했을 경우에도 가게 entity 의 updateDate 를 갱신하기 위해서 isUpdate 값을 true 로 줌.
                entity.addFiles(fileEntity, true);  // 가게 엔티티와 파일 엔티티 관계를 맺어줌
            } else {
                throw new RuntimeException("메인 이미지 업로드 중 오류가 발생했습니다.");
            }
        }

        List<MultipartFile> imageList = request.getImage();
        boolean isFirst = true;   // 기타 이미지 파일 업로드를 처음 시도하는지 여부

        // 2. 업로드 할 기타 이미지 파일이 있으면 업로드
        if (imageList != null && imageList.size() > 0) {
            for (MultipartFile image : imageList) {
                if (image != null && !image.isEmpty()) {
                    // 2-1. 파일 업로드
                    Map<String, Object> fileMap = fileUtils.uploadImageFiles(image, filePath);   // 파일 업로드 과정 공통화해서 분리

                    if (isFirst) {   // 기타 이미지 파일 업로드를 처음 시도하는 경우는...
                        entity.getFileList().removeIf(file -> "N".equals(file.getMainYn()));   // 기타 이미지만 삭제
                        isFirst = false;
                    }

                    // 2-2. 파일 등록
                    // 파일이 있을 경우에만 파일 엔티티 생성
                    if (fileMap != null) {  
                        StoreFileEntity fileEntity = new StoreFileEntity();
                        fileEntity.setFileName(fileMap.get("fileName").toString());
                        fileEntity.setStoredName(fileMap.get("storedFileName").toString());
                        fileEntity.setFilePath(fileMap.get("filePath").toString());
                        fileEntity.setFileThumbName(fileMap.get("thumbName").toString());
                        fileEntity.setFileSize(image.getSize());
                        fileEntity.setMainYn("N");

                        // 파일만 수정했을 경우에도 가게 entity 의 updateDate 를 갱신하기 위해서 isUpdate 값을 true 로 줌.
                        entity.addFiles(fileEntity, true);  // 가게 엔티티와 파일 엔티티 관계를 맺어줌
                    } else {
                        throw new RuntimeException("기타 이미지 업로드 중 오류가 발생했습니다.");
                    }
                }
            }
        }

        storeRepository.save(entity);

        if (request.getMainImage() != null && !request.getMainImage().isEmpty()) {
            // 2-3. 기존 파일 삭제 (작업 도중 DB에 문제가 생길 수도 있기 때문에 물리적 파일 삭제는 제일 마지막에 진행)
            // 가게 상세 정보 DTO 가 가지고 있는 파일 DTO 리스트 순회
            if (detail.getFileList() != null && detail.getFileList().size() > 0) {
                for (StoreFileDTO fileDTO : detail.getFileList()) {
                    if (fileDTO.getMainYn().equals("Y")) {
                        deleteImageFiles(fileDTO);

                        break;   // 메인 이미지 파일은 하나뿐이기 때문에 지웠다면 반복문을 더 순회할 필요가 없기 때문에 바로 break;
                    }
                }
            }
        }

        if (imageList != null && imageList.size() > 0) {
            for (MultipartFile image : imageList) {
                if (image != null && !image.isEmpty()) {
                    // 2-3. 기존 파일 삭제 (작업 도중 DB에 문제가 생길 수도 있기 때문에 물리적 파일 삭제는 제일 마지막에 진행)
                    // 가게 상세 정보 DTO 가 가지고 있는 파일 DTO 리스트 순회
                    if (detail.getFileList() != null && detail.getFileList().size() > 0) {
                        for (StoreFileDTO fileDTO : detail.getFileList()) {
                            if (fileDTO.getMainYn().equals("N")) {
                                deleteImageFiles(fileDTO);
                            }
                        }
                        break;
                    }
                }
            }
        }
    }

    /**
     * 가게 삭제(실제로 삭제되지는 않음. 삭제 여부만 변경)
     * @param storeId 가게 아이디
     * @return
     * @throws Exception
     */
    @Transactional
    public void deleteStore(int storeId) throws Exception {

        StoreEntity entity = storeRepository.getStore(storeId)   // fetch join 을 사용한 getStore 메서드 호출
            .orElseThrow(()-> new RuntimeException("해당 가게가 존재하지 않습니다."));
        entity.setDelYn("Y");  // 삭제 여부 Y로 변경    

        storeRepository.save(entity);

        // 모든 사람들의 장바구니에서 해당 가게 내용들 전부 삭제하기
        //basketRepository.deleteAllByStore_storeId(entity.getStoreId());

        // 가게의 삭제 여부를 Y 로 변경만하고 가게의 이미지들은 삭제하지 않음
        // 왜냐하면 주문 내역 등에서 해당 가게의 상세 정보를 보여줘야 하기 때문
    }

    /**
     * 파일 삭제과정 공통화해서 분리
     * @param dto 가게 파일 정보 dto
     * @throws Exception
     */
    private void deleteImageFiles(StoreFileDTO dto) throws Exception {
        // 파일 정보
        String fullPath = dto.getFilePath() + dto.getStoredName();
        String thumbFilePath = filePath + "thumb" + File.separator + dto.getFileThumbName();

        try {
            File file = new File(fullPath);

            if (file.exists()) {
                // 원본 파일 삭제
                fileUtils.deleteFile(fullPath);
            } else {
                log.warn("삭제하려는 원본 파일이 없습니다. path={}", fullPath);
            }

            File thumbFile = new File(thumbFilePath);

            if (thumbFile.exists()) {
                // 썸네일 파일 삭제
                fileUtils.deleteFile(thumbFilePath);
            } else {
                log.warn("삭제하려는 썸네일 파일이 없습니다. path={}", thumbFilePath);
            }

        } catch (Exception e) {
            // 파일 삭제 실패 시 전체 트랜잭션을 깨뜨리지 않도록 함
            log.error("파일 삭제 중 오류가 발생했습니다. {}", e.getMessage(), e);
        }
    }
}
