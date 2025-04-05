package org.example.szs.domain.taxinfo;

import java.util.List;

import org.example.szs.common.error.BusinessException;
import org.example.szs.common.error.ErrorCode;
import org.example.szs.domain.user.EncryptedRegNo;
import org.example.szs.domain.user.RegNoEncryptor;
import org.example.szs.domain.user.User;
import org.example.szs.domain.user.UserRepository;
import org.example.szs.infra.auth.LoginUser;
import org.example.szs.infra.feign.client.ScrapClient;
import org.example.szs.infra.feign.dto.ScrapApiResponse;
import org.example.szs.infra.feign.dto.ScrapApiStatus;
import org.example.szs.infra.feign.dto.ScrapRequest;
import org.springframework.stereotype.Service;

import feign.FeignException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RequiredArgsConstructor
@Service
public class TaxInfoService {

}
